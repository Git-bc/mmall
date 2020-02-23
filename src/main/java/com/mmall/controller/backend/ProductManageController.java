package com.mmall.controller.backend;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.vo.ProductDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IProductService iProductService;
    @Autowired
    private IFileService iFileService;

    /**
     * 保存或更新产品
     * @param session
     * @param product
     * @return
     */
    @RequestMapping(value = "product_save.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse productSave(HttpSession session, Product product){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponse.createByErrorMessage("用户未登录,请登录管理员账号");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iProductService.saveOrUpdateProduct(product);
        }else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }

    }

    /**
     * 改变商品状态
     * @param session
     * @param productId
     * @param status
     * @return
     */
    @RequestMapping(value = "set_sale_status.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setSaleStatus(HttpSession session, Integer productId,Integer status){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponse.createByErrorMessage("用户未登录,请登录管理员账号");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iProductService.setSaleStatus(productId,status);
        }else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }

    }

    /**
     * 获取商品详情
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping(value = "get_detail.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<ProductDetailVo> getDetail(HttpSession session, Integer productId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponse.createByErrorMessage("用户未登录,请登录管理员账号");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iProductService.manageProductDetail(productId);
        }else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }

    }

    /**
     * 获取商品列表(分页)
     * @param session
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "list.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse productSave(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,@RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponse.createByErrorMessage("用户未登录,请登录管理员账号");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iProductService.getProductList(pageNum, pageSize);
        }else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }

    }

    /**
     * 查询商品(模糊查询)
     * @param session
     * @param productName
     * @param productId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "search.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse productSearch(HttpSession session,String productName,Integer productId,@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,@RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponse.createByErrorMessage("用户未登录,请登录管理员账号");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iProductService.searchProduct(productName, productId, pageNum, pageSize);
        }else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }

    }

    /**
     * 图片上传
     * @param file
     * @param request
     * @return
     */
    @RequestMapping(value = "upload.do")
    @ResponseBody
    public ServerResponse upload(@RequestParam(value = "upload_file",required = false)MultipartFile file, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponse.createByErrorMessage("用户未登录,请登录管理员账号");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iFileService.upload(file);
        }else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    /**
     * 富文本图片上传
     * @param file
     * @param session
     * @param res
     * @return
     */
    @RequestMapping(value = "rich_text_img_upload.do")
    @ResponseBody
    public Map richTextImgUpload(@RequestParam(value = "upload_file",required = false) MultipartFile file, HttpSession session, HttpServletResponse res){
        Map resultMap = Maps.newHashMap();
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            resultMap.put("success",false);
            resultMap.put("msg", "用户未登录,请登录管理员账号");
            return resultMap;
        }
        //富文本中对于返回值有自自己的要求，我们使用的是simditor富文本插件，所以按照simditor的要求进行返回
        if(iUserService.checkAdminRole(user).isSuccess()){
            ServerResponse<Map> response = iFileService.upload(file);
            if (response.isSuccess()){
                resultMap.put("success",true);
                resultMap.put("msg", "上传成功");
                resultMap.put("file_path", response.getData().get("url"));
                res.addHeader("Access-Control-Allow-Headers", "X-File-Name");
                return resultMap;
            }
            resultMap.put("success",true);
            resultMap.put("msg", "上传失败");
            return resultMap;
        }else {
            resultMap.put("success",false);
            resultMap.put("msg", "无权限操作");
            return resultMap;
        }
    }

}
