package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckGroupDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service(interfaceClass = CheckGroupService.class)
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    /**
     * 添加检查组
     *
     * @param checkGroup
     * @param checkitemIds
     */
    @Override
    @Transactional
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        // dao添加检查组到数据
        checkGroupDao.add(checkGroup);
        // 获取检查组的编号
        Integer checkGroupId = checkGroup.getId();
        // 设置检查组与检查项的关系
        if (null != checkitemIds) {
            for (Integer checkitemId : checkitemIds) {
                // 设置检查组与检查项的关系, 往t_checkgroup_checkitem
                checkGroupDao.setCheckGroupAndCheckItem(checkGroupId, checkitemId);
            }
        }
    }

    /**
     * 分页条件查询
     *
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult<CheckGroup> findPage(QueryPageBean queryPageBean) {
        // 实现模糊查询，拼接%
        if (!StringUtils.isEmpty(queryPageBean.getQueryString())) {
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        // 紧接着的查询会被分页
        Page<CheckGroup> page = checkGroupDao.findByCondition(queryPageBean.getQueryString());

        // 封装返回的结果
        PageResult<CheckGroup> pageResult = new PageResult<>(page.getTotal(),page.getResult());
        return pageResult;
    }

    /**
     * 通过编号查询检查组
     * @param id
     * @return
     */
    @Override
    public CheckGroup findById(int id) {
        return checkGroupDao.findById(id);
    }

    /**
     * 通过检查组的编号查询选中的检查项编号
     * @param id
     * @return
     */
    @Override
    public List<Integer> getCheckItemIds(int id) {
        return checkGroupDao.getCheckItemIds(id);
    }

    /**
     * 修改检查组
     * @param checkGroup
     * @param checkitemIds
     */
    @Override
    @Transactional
    public void update(CheckGroup checkGroup, Integer[] checkitemIds) {
        //- 更新检查组信息
        checkGroupDao.update(checkGroup);
        //- 更新检查组与检查项的关系设置
        //  - 先删除关系 delete from t_checkgroup_checkitem where checkgroup_id=checkGroupId
        // 检查组的编号
        int checkGroupId = checkGroup.getId();
        checkGroupDao.deleteCheckGroupCheckItemByCheckGroupId(checkGroupId);

        //  - 再添加新的关系checkitemIds
        //    非空判断，遍历查检项的id数组，往t_checkgroup_checkitem添加记录
        if(null != checkitemIds){
            for (Integer checkitemId : checkitemIds) {
                // 设置检查组与检查项的关系, 往t_checkgroup_checkitem
                checkGroupDao.setCheckGroupAndCheckItem(checkGroupId, checkitemId);
            }
        }
    }

    /**
     * 获取检查组所有数据
     * @return
     */
    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }
}
