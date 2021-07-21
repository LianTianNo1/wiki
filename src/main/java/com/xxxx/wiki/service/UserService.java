package com.xxxx.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.wiki.domain.User;
import com.xxxx.wiki.domain.UserExample;
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
        // PageHelper分页插件
        // 这句话这对第一个遇到的select起作用
        PageHelper.startPage(req.getPage(),req.getSize());
        List<User> userList = userMapper.selectByExample(userExample);

        PageInfo<User> pageInfo = new PageInfo<>(userList);
        // 获取总行数
        LOG.info("总行数，{}", pageInfo.getTotal());
        // 获取总页数
        LOG.info("总页数，{}", pageInfo.getPages());

//        List<UserResp> respList = new ArrayList<>();
//        //  iter （live template）
//        for (User user : userList) {
//            UserResp userResp = new UserResp();
//            //  Spring提供的工具类
//            BeanUtils.copyProperties(user, userResp);
//            respList.add(userResp);
//        }
//
//        上面这段代码可以用下面这段代码代替

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
            // 新增
            user.setId(snowFlake.nextId());
            userMapper.insert(user);
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
}
