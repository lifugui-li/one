package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.CheckItemDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List;

@Service(interfaceClass = CheckItemService.class)
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;

    /**
     * 新增检查项
     * @param checkItem
     */
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult<CheckItem> findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        //要实现模糊查询，就得拼接%
        if(!StringUtils.isEmpty(queryPageBean.getQueryString())){
            // 有查询条件时，拼接%
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }

        // 使用分页插件
        PageHelper.startPage(currentPage-1, queryPageBean.getPageSize());
        // 紧接着的查询语句会被拦截进行分页，要查询所有
        Page<CheckItem> page = checkItemDao.findByCondition(queryPageBean.getQueryString());
        // PageResult 用PageResult包装
        PageResult<CheckItem> result = new PageResult<CheckItem>(page.getTotal(),page.getResult());
        return result;
    }

    /**
     * 通过编号删除
     * @param id
     */
    @Override
    public void deleteById(int id) {
        // 判断是否有引用，当前的检查项是否有检查组在用它
        // 通过查询t_checkgroup_checkitem where checkitem_id=要删除的编号
        int count = checkItemDao.countByCheckItemId(id);
        if(count > 0){
            // 有引用了, 自定义异常：使用场景，终止已知不符合业务逻辑的操作继续执行
            throw new RuntimeException(MessageConstant.DELETE_CHECKITEM_FAIL_USED);
        }
        checkItemDao.deleteById(id);
    }

    /**
     * 通过编号查询
     * @param id
     * @return
     */
    @Override
    public CheckItem findById(int id) {
        return checkItemDao.findById(id);
    }

    /**
     * 更新
     * @param checkItem
     */
    @Override
    public void update(CheckItem checkItem) {
        checkItemDao.update(checkItem);
    }


    /**
     * 查询所有检查项列表
     * @return
     */
    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }
}
