package com.donnan.git.guru.user.domain.service;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.CacheManager;
import com.alicp.jetcache.anno.CacheInvalidate;
import com.alicp.jetcache.anno.CacheRefresh;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.alicp.jetcache.template.QuickConfig;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.donnan.git.guru.api.user.request.UserModifyRequest;
import com.donnan.git.guru.api.user.response.UserOperatorResponse;
import com.donnan.git.guru.base.response.PageResponse;
import com.donnan.git.guru.lock.DistributeLock;
import com.donnan.git.guru.user.domain.entity.User;
import com.donnan.git.guru.user.infrastructure.exception.UserErrorCode;
import com.donnan.git.guru.user.infrastructure.exception.UserException;
import com.donnan.git.guru.user.infrastructure.mapper.UserMapper;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static com.donnan.git.guru.user.infrastructure.exception.UserErrorCode.*;


/**
 * 用户服务
 * @author Donnan
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    private static final String DEFAULT_NICK_NAME_PREFIX = "藏家_";

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private UserCacheDelayDeleteService userCacheDelayDeleteService;

    //用户名布隆过滤器
    private RBloomFilter<String> nickNameBloomFilter;

    //通过用户ID对用户信息做的缓存
    private Cache<String, User> idUserCache;

    @PostConstruct
    public void init() {
        QuickConfig idQc = QuickConfig.newBuilder(":user:cache:id:")
                .cacheType(CacheType.BOTH)
                .expire(Duration.ofHours(2))
                .syncLocal(true)
                .build();
        idUserCache = cacheManager.getOrCreateCache(idQc);
    }

    @DistributeLock(keyExpression = "#telephone", scene = "USER_REGISTER")
    @Transactional
    public UserOperatorResponse register(String telephone) {
        String defaultNickName;
        String randomString;
        do {
            randomString = RandomUtil.randomString(6).toUpperCase();
            //前缀 + 6位随机数 + 手机号后四位
            defaultNickName = DEFAULT_NICK_NAME_PREFIX + randomString + telephone.substring(7, 11);
        } while (nickNameExist(defaultNickName));

        User user = register(telephone, defaultNickName, telephone, randomString);
        Assert.notNull(user, UserErrorCode.USER_OPERATE_FAILED.getCode());

        addNickName(defaultNickName);
        updateUserCache(user.getId().toString(), user);

        UserOperatorResponse userOperatorResponse = new UserOperatorResponse();
        userOperatorResponse.setSuccess(true);

        return userOperatorResponse;
    }

    /**
     * 管理员注册
     *
     * @param telephone
     * @param password
     * @return
     */
    @DistributeLock(keyExpression = "#telephone", scene = "USER_REGISTER")
    @Transactional
    public UserOperatorResponse registerAdmin(String telephone, String password) {
        User user = registerAdmin(telephone, telephone, password);
        Assert.notNull(user, UserErrorCode.USER_OPERATE_FAILED.getCode());
        idUserCache.put(user.getId().toString(), user);

        UserOperatorResponse userOperatorResponse = new UserOperatorResponse();
        userOperatorResponse.setSuccess(true);

        return userOperatorResponse;
    }

    /**
     * 注册
     *
     * @param telephone
     * @param nickName
     * @param password
     * @return
     */
    private User register(String telephone, String nickName, String password, String inviterId) {
        if (userMapper.findByTelephone(telephone) != null) {
            throw new UserException(DUPLICATE_TELEPHONE_NUMBER);
        }

        User user = new User();
        user.register(telephone, nickName, password, inviterId);
        return save(user) ? user : null;
    }

    private User registerAdmin(String telephone, String nickName, String password) {
        if (userMapper.findByTelephone(telephone) != null) {
            throw new UserException(DUPLICATE_TELEPHONE_NUMBER);
        }

        User user = new User();
        user.registerAdmin(telephone, nickName, password);
        return save(user) ? user : null;
    }

    /**
     * 分页查询用户信息
     *
     * @param keyWord
     * @param state
     * @param currentPage
     * @param pageSize
     * @return
     */
    public PageResponse<User> pageQueryByState(String keyWord, String state, int currentPage, int pageSize) {
        Page<User> page = new Page<>(currentPage, pageSize);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("state", state);

        if (keyWord != null) {
            wrapper.like("telephone", keyWord);
        }
        wrapper.orderBy(true, true, "gmt_create");

        Page<User> userPage = this.page(page, wrapper);

        return PageResponse.of(userPage.getRecords(), (int) userPage.getTotal(), pageSize, currentPage);
    }

    /**
     * 通过手机号和密码查询用户信息
     *
     * @param telephone
     * @param password
     * @return
     */
    public User findByTelephoneAndPass(String telephone, String password) {
        return userMapper.findByTelephoneAndPass(telephone, DigestUtil.md5Hex(password));
    }

    /**
     * 通过手机号查询用户信息
     *
     * @param telephone
     * @return
     */
    public User findByTelephone(String telephone) {
        return userMapper.findByTelephone(telephone);
    }

    /**
     * 通过用户ID查询用户信息
     *
     * @param userId
     * @return
     */
    @Cached(name = ":user:cache:id:", cacheType = CacheType.BOTH, key = "#userId", cacheNullValue = true)
    @CacheRefresh(refresh = 60, timeUnit = TimeUnit.MINUTES)
    public User findById(Long userId) {
        return userMapper.findById(userId);
    }

    /**
     * 更新用户信息
     *
     * @param userModifyRequest
     * @return
     */
    @CacheInvalidate(name = ":user:cache:id:", key = "#userModifyRequest.userId")
    @Transactional
    public UserOperatorResponse modify(UserModifyRequest userModifyRequest) {
        UserOperatorResponse userOperatorResponse = new UserOperatorResponse();
        User user = userMapper.findById(userModifyRequest.getUserId());
        Assert.notNull(user, () -> new UserException(USER_NOT_EXIST));
        Assert.isTrue(user.canModifyInfo(), () -> new UserException(USER_STATUS_CANT_OPERATE));

        if (StringUtils.isNotBlank(userModifyRequest.getNickName()) && nickNameExist(userModifyRequest.getNickName())) {
            throw new UserException(NICK_NAME_EXIST);
        }
        BeanUtils.copyProperties(userModifyRequest, user);

        if (StringUtils.isNotBlank(userModifyRequest.getPassword())) {
            user.setPasswordHash(DigestUtil.md5Hex(userModifyRequest.getPassword()));
        }
        if (updateById(user)) {
            addNickName(userModifyRequest.getNickName());
            userOperatorResponse.setSuccess(true);

            return userOperatorResponse;
        }
        userOperatorResponse.setSuccess(false);
        userOperatorResponse.setResponseCode(UserErrorCode.USER_OPERATE_FAILED.getCode());
        userOperatorResponse.setResponseCode(UserErrorCode.USER_OPERATE_FAILED.getMessage());

        return userOperatorResponse;
    }

    public PageResponse<User> getUsersByInviterId(String inviterId, int currentPage, int pageSize) {
        Page<User> page = new Page<>(currentPage, pageSize);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.select("nick_name", "gmt_create");
        wrapper.eq("inviter_id", inviterId);

        wrapper.orderBy(true, false, "gmt_create");

        Page<User> userPage = this.page(page, wrapper);
        return PageResponse.of(userPage.getRecords(), (int) userPage.getTotal(), pageSize, currentPage);
    }

    public boolean nickNameExist(String nickName) {
        //如果布隆过滤器中存在，再进行数据库二次判断
        if (this.nickNameBloomFilter != null && this.nickNameBloomFilter.contains(nickName)) {
            return userMapper.findByNickname(nickName) != null;
        }

        return false;
    }

    private boolean addNickName(String nickName) {
        return this.nickNameBloomFilter != null && this.nickNameBloomFilter.add(nickName);
    }

    private void updateUserCache(String userId, User user) {
        idUserCache.put(userId, user);
    }
}
