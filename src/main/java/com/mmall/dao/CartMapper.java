package com.mmall.dao;

import com.mmall.pojo.Cart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    Cart selCartByUIdAndPId(@Param("userId") Integer userId,@Param("productId") Integer productId);

    List<Cart> selCartByUserId(Integer userId);

    @Select("select count(1) from mmall_cart where checked = 0 and user_id = #{userId}")
    int selCartProductCheckedStatusByUserId(Integer userId);

    int delByUIdAndPIds(@Param("userId") Integer userId,@Param("productIdList") List<String> productIdList);

    int checkedOrUncheckedProduct(@Param("userId") Integer userId,@Param("productId") Integer productId,@Param("checked") Integer checked);

    @Select("select ifnull(sum(quantity),0) as count from mmall_cart where user_id = #{userId}")
    int selectCartProductCount(Integer userId);
}