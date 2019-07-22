package com.itheima.dao;

import com.itheima.pojo.Package;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface PackageDao {
    /**
     * 添加套餐
     * @param pkg
     */
    void add(Package pkg);

    /**
     * 设置套餐与检查组的关系
     * @param pkgId
     * @param checkgroupId
     */
    void setPackageAndCheckGroup(@Param("pkgId") Integer pkgId, @Param("checkgroupId") Integer checkgroupId);

    /**
     * 查询所有套餐列表
     * @return
     */
    List<Package> findAll();

    /**
     * 通过编号查询套餐信息，同时要查询出所对应的检查组和检查项信息
     * @param id
     * @return
     */
    Package findById(int id);

    /**
     * 通过编号查询套餐信息，只要套餐信息就可以了，不需要详细信息
     * @param id
     * @return
     */
    Package findPackageById(int id);

    /**
     * 套餐预约数量统计
     * @return
     */
    List<Map<String,Object>> getPackageReport();

    /**
     * 热门套餐列表
     * @return
     */
    List<Map<String,Object>> getHotPackages();
}
