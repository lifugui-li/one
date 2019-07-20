package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.pojo.Package;
import com.itheima.dao.PackageDao;
import com.itheima.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service(interfaceClass = PackageService.class)
public class PackageServiceImpl implements PackageService {

    @Autowired
    private PackageDao packageDao;

    @Override
    @Transactional
    public void add(Package pkg, Integer[] checkgroupIds) {
        // 添加到套餐表
        packageDao.add(pkg);
        // 取出套餐的编号
        Integer pkgId = pkg.getId();
        // 设置套餐与检查组的关系
        if(null != checkgroupIds){
            for (Integer checkgroupId : checkgroupIds) {
                packageDao.setPackageAndCheckGroup(pkgId, checkgroupId);
            }
        }
    }

    /**
     * 查询所有的套餐
     * @return
     */
    @Override
    public List<Package> findAll() {
        return packageDao.findAll();
    }

    /**
     * 通过编号查询套餐信息，同时要查询出所对应的检查组和检查项信息
     * @param id
     * @return
     */
    @Override
    public Package findById(int id) {
        Package pkg = packageDao.findById(id);
        /*// 查询套餐所包含的检查组信息
        List<CheckGroup> checkGroups = checkGroupDao.findByPackageId(id);
        pkg.setCheckGroups(checkGroups);
        // 检查组下还检查项
        for (CheckGroup checkGroup : checkGroups) {
            // 通过检查组编号查询检查项
            List<CheckItem> items = checkItemDao.findByCheckGroupId(checkGroup.getId());
            checkGroup.setCheckItems(items);
        }*/
        return pkg;
    }

    /**
     * 通过编号查询套餐信息，只要套餐信息就可以了，不需要详细信息
     * @param id
     * @return
     */
    @Override
    public Package findPackageById(int id) {
        return packageDao.findPackageById(id);
    }

    /**
     * 套餐预约数量统计
     * @return
     */
    @Override
    public List<Map<String, Object>> getPackageReport() {
        return packageDao.getPackageReport();
    }
}
