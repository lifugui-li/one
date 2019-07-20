package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.Util.DateUtils;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    @Override
    @Transactional
    public Result addOrder(Map<String, Object> map) throws Exception {
        // 预约日期
        String orderDate = (String) map.get("orderDate");

        // 手机号码
        String telephone = (String) map.get("telephone");
        // - 通过日期查询预约
        Date order_date = DateUtils.parseString2Date(orderDate);
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(order_date);
        //- 判断是否可以预约
        //  - 这个日期没有预约设置
        if (null == orderSetting) {
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //  - 预约已经满了
        if (orderSetting.getNumber() <= orderSetting.getReservations()) {
            return new Result(false, MessageConstant.ORDER_FULL);
        }

        Order order = new Order();
        // 预约日期
        order.setOrderDate(order_date);
        // 套餐编号
        order.setPackageId(Integer.valueOf((String) map.get("packageId")));
        //- 判断是否是会员, 通过手机号码
        Member member = memberDao.findByTelephone(telephone);
        //  - 是，取出会员编号，用重复预约判断
        if (null != member) {
            //- 判断是否重复预约
            //  - 查询t_order条件会员编号，预约日期，套餐ID
            order.setMemberId(member.getId());
            List<Order> orderList = orderDao.findByCondition(order);
            if (orderList != null && orderList.size() > 0) {
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        } else {
            //  - 否，添加为会员，取会员编号
            member = new Member();
            member.setPhoneNumber(telephone);
            member.setName((String) map.get("name"));
            member.setRegTime(new Date());
            member.setSex((String) map.get("sex"));
            member.setIdCard((String) map.get("idCard"));
            // 添加新会员
            memberDao.add(member);
            // 设置订单的会员编号
            order.setMemberId(member.getId());
        }
        //  - 没值，可以预约
        //    - 往t_order添加记录
        order.setOrderType((String) map.get("orderType"));
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        orderDao.add(order);
        //    - 更新已经预约的数量
        orderSetting.setReservations(orderSetting.getReservations() + 1);
        orderSettingDao.editReservationsByOrderDate(orderSetting);
        return new Result(true, MessageConstant.ORDER_SUCCESS, order.getId());
    }

    /**
     * 查询预约信息
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> findById(int id) {
        return orderDao.findById4Detail(id);
    }
}
