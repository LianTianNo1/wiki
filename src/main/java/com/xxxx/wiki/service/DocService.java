package com.xxxx.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.wiki.domain.Content;
import com.xxxx.wiki.domain.Doc;
import com.xxxx.wiki.domain.DocExample;
import com.xxxx.wiki.exception.BusinessException;
import com.xxxx.wiki.exception.BusinessExceptionCode;
import com.xxxx.wiki.mapper.ContentMapper;
import com.xxxx.wiki.mapper.DocMapper;
import com.xxxx.wiki.mapper.DocMapperCust;
import com.xxxx.wiki.req.DocQueryReq;
import com.xxxx.wiki.req.DocSaveReq;
import com.xxxx.wiki.resp.DocQueryResp;
import com.xxxx.wiki.resp.PageResp;
import com.xxxx.wiki.util.CopyUtil;
import com.xxxx.wiki.util.RedisUtil;
import com.xxxx.wiki.util.RequestContext;
import com.xxxx.wiki.util.SnowFlake;
import com.xxxx.wiki.websocket.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by stfu
 * 2021-07-08 16:30
 */
@Service
public class DocService {

    private static final Logger LOG = LoggerFactory.getLogger(DocService.class);

    @Resource
    private DocMapper docMapper;

    @Resource
    private DocMapperCust docMapperCust;

    @Resource
    private ContentMapper contentMapper;

    @Resource
    private WsService wsService;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private SnowFlake snowFlake;

    @Resource
    private WebSocketServer webSocketServer;

    /**
     * 查询全部
     * @return
     * @param ebookId
     */
    public List<DocQueryResp> all(Long ebookId) {
        DocExample docExample = new DocExample();
        docExample.createCriteria().andEbookIdEqualTo(ebookId);
        docExample.setOrderByClause("sort asc");
        List<Doc> docList = docMapper.selectByExample(docExample);

        List<DocQueryResp> respList = CopyUtil.copyList(docList, DocQueryResp.class);

        return respList;
    }

    /**
     * 分页查询
     * @param req
     * @return
     */
    public PageResp<DocQueryResp> list(DocQueryReq req) {
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        DocExample.Criteria criteria = docExample.createCriteria();

        PageHelper.startPage(req.getPage(),req.getSize());
        List<Doc> docList = docMapper.selectByExample(docExample);

        PageInfo<Doc> pageInfo = new PageInfo<>(docList);

        LOG.info("总行数，{}", pageInfo.getTotal());
        LOG.info("总页数，{}", pageInfo.getPages());

        List<DocQueryResp> respList = CopyUtil.copyList(docList, DocQueryResp.class);

        PageResp<DocQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(respList);

        return pageResp;
    }

    /**
     * 保存
     * @param req
     */
    public void save(DocSaveReq req) {
        Doc doc = CopyUtil.copy(req, Doc.class);
        // 只会copy名字对应的字段，比如这个是content字段与id字段
        Content content = CopyUtil.copy(req, Content.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            // 新增
            doc.setId(snowFlake.nextId());
            doc.setViewCount(0);
            doc.setVoteCount(0);
            docMapper.insert(doc);

            //为了保持与上面的id一致，所以doc.getId()
            content.setId(doc.getId());
            contentMapper.insert(content);
        } else {
            // 更新
            docMapper.updateByPrimaryKey(doc);
            // 带上大字段的用法
            int count = contentMapper.updateByPrimaryKeyWithBLOBs(content);
            // 更新文档时可能它的content为空，用上面的根据id更新就会失败
            if (count == 0) {
                contentMapper.insert(content);
            }
        }
    }

    /**
     * 删除
     * @param id
     */
    public void delete(Long id) {
        docMapper.deleteByPrimaryKey(id);
    }

    /**
     * 批量删除
     * @param ids
     */
    public void delete(List<String> ids) {
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andIdIn(ids);
        docMapper.deleteByExample(docExample);
    }

    /**
     * 富文本内容查询
     * @param id
     * @return
     */
    public String findContent(Long id) {
        Content content = contentMapper.selectByPrimaryKey(id);
        //  文档阅读数加一
        docMapperCust.increaseViewCount(id);
        if (ObjectUtils.isEmpty(content)) {
            return "";
        }
        return content.getContent();
    }

    /**
     * 点赞
     * @param id
     */
    public void vote(Long id) {
        // doc.id + 远程IP 作为key，过期时间设置为24小时
        String ip = RequestContext.getRemoteAddr();
        if (redisUtil.validateRepeat("DOC_VOTE_" + id + "_" + ip, 3600 * 24)) {
            docMapperCust.increaseVoteCount(id);
        } else {
            throw new BusinessException(BusinessExceptionCode.VOTE_REPEAT);
        }

        // 推送消息
        Doc docDB = docMapper.selectByPrimaryKey(id);
        //  将日志流水号传给ws线程
        String logId = MDC.get("LOG_ID");
        wsService.sendInfo("【" + docDB.getName() + "】被点赞！", logId);
    }



    /**
     * 更新电子书信息
     */
    public void updateEbookInfo()  {
        docMapperCust.updateEbookInfo();
    }
}
