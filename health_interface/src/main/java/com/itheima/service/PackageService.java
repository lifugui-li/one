package com.itheima.service;

import com.itheima.pojo.Package;

import java.util.List;
import java.util.Map;

public interface PackageService {
    /**
     * 添加套餐
     * @param pkg
     * @param checkgroupIds
     */
    void add(Package pkg, Integer[] checkgroupIds);

    /**
     * 查询所有的套餐
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
}
