package com.xxxx.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.wiki.domain.User;
import com.xxxx.wiki.domain.UserExample;
import com.xxxx.wiki.exception.BusinessException;
import com.xxxx.wiki.exception.BusinessExceptionCode;
import com.xxxx.wiki.mapper.UserMapper;
import com.xxxx.wiki.req.UserQueryReq;
import com.xxxx.wiki.req.UserSaveReq;
import com.xxxx.wiki.resp.UserQueryResp;
import com.xxxx.wiki.resp.PageResp;
import com.xxxx.wiki.util.CopyUtil;
import com.xxxx.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by stfu
 * 2021-07-08 16:30
 */
@Service
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Resource
    private UserMapper userMapper;

    @Resource
    private SnowFlake snowFlake;

    /**
     * 查询
     * @param req
     * @return
     */
    public PageResp<UserQueryResp> list(UserQueryReq req) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getLoginName())) {
            criteria.andLoginNameLike("%" + req.getLoginName() + "%");
        }
        PageHelper.startPage(req.getPage(),req.getSize());
        List<User> userList = userMapper.selectByExample(userExample);

        PageInfo<User> pageInfo = new PageInfo<>(userList);

        LOG.info("总行数，{}", pageInfo.getTotal());
        LOG.info("总页数，{}", pageInfo.getPages());

        // 列表复制
        List<UserQueryResp> respList = CopyUtil.copyList(userList, UserQueryResp.class);

        PageResp<UserQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(respList);

        return pageResp;
    }

    /**
     * 保存
     * @param req
     */
    public void save(UserSaveReq req) {
        User user = CopyUtil.copy(req, User.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            //  检查用户名是否已经存在
            if (ObjectUtils.isEmpty(selectByLoginName(req.getLoginName()))) {
                // 新增
                user.setId(snowFlake.nextId());
                userMapper.insert(user);
            } else {
                //  用户名已经存在
                //  抛出自定义异常
                throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
            }
        } else {
            // 更新
            userMapper.updateByPrimaryKey(user);
        }
    }

    /**
     * 删除
     * @param id
     */
    public void delete(Long id) {
        userMapper.deleteByPrimaryKey(id);
    }

    private User selectByLoginName(String LoginName) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andLoginNameEqualTo(LoginName);
        List<User> userList = userMapper.selectByExample(userExample);
        if (CollectionUtils.isEmpty(userList)) {
            return null;
        } else {
            return userList.get(0);
        }
    }
}
