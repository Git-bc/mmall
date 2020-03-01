package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private ICategoryService iCategoryService;

    /**
     * 添加产品类别
     * @param session session
     * @param categoryName categoryName
     * @param parentId parentId
     * @return return
     */
    @RequestMapping(value = "add_category.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse addCategory(HttpSession session,String categoryName,@RequestParam(value = "parentId",defaultValue = "0") int parentId){
        return iCategoryService.addCategory(categoryName, parentId);
    }

    /**
     * 修改类别名称
     * @param session session
     * @param categoryId categoryId
     * @param categoryName categoryName
     * @return return
     */
    @RequestMapping(value = "set_category_name.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setCategoryName(HttpSession session,int categoryId,String categoryName){
        return iCategoryService.updateCategoryName(categoryId, categoryName);
    }

    /**
     * 查询当前分类的子分类
     * @param session session
     * @param categoryId categoryId
     * @return return
     */
    @RequestMapping(value = "get_children_parallel_category.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<List<Category>> getChildrenParallelCategory(HttpSession session, @RequestParam(value = "categoryId",defaultValue = "0") int categoryId){
        return iCategoryService.getChildrenParallelCategory(categoryId);
    }

    /**
     * 获取当前品类Id及子孙品类Id
     * @param session session
     * @param categoryId categoryId
     * @return return return
     */
    @RequestMapping(value = "get_deep_category.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<List<Integer>> getCategoryAndDeepChildrenCategory(HttpSession session,int categoryId){
        return iCategoryService.selectCategoryAndChildrenById(categoryId);
    }
}
