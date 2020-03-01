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
        return iProductService.saveOrUpdateProduct(product);
    }

    /**
     * 改变商品状态
     * @param session session
     * @param productId productId
     * @param status status
     * @return return
     */
    @RequestMapping(value = "set_sale_status.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setSaleStatus(HttpSession session, Integer productId,Integer status){
        return iProductService.setSaleStatus(productId,status);
    }

    /**
     * 获取商品详情
     * @param session session
     * @param productId productId
     * @return return
     */
    @RequestMapping(value = "get_detail.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<ProductDetailVo> getDetail(HttpSession session, Integer productId){
        return iProductService.manageProductDetail(productId);
    }

    /**
     * 获取商品列表(分页)
     * @param session session
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return return
     */
    @RequestMapping(value = "list.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse productSave(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,@RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        return iProductService.getProductList(pageNum, pageSize);
    }

    /**
     * 查询商品(模糊查询)
     * @param session session
     * @param productName productName
     * @param productId productId
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return return
     */
    @RequestMapping(value = "search.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse productSearch(HttpSession session,String productName,Integer productId,@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,@RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        return iProductService.searchProduct(productName, productId, pageNum, pageSize);

    }

    /**
     * 图片上传
     * @param file file
     * @param request request
     * @return return
     */
    @RequestMapping(value = "upload.do")
    @ResponseBody
    public ServerResponse upload(@RequestParam(value = "upload_file",required = false)MultipartFile file, HttpServletRequest request){
            return iFileService.upload(file);
    }

    /**
     * 富文本图片上传
     * @param file file
     * @param session session
     * @param res res
     * @return return
     */
    @RequestMapping(value = "rich_text_img_upload.do")
    @ResponseBody
    public Map richTextImgUpload(@RequestParam(value = "upload_file",required = false) MultipartFile file, HttpSession session, HttpServletResponse res){
        Map<String,Object> resultMap = Maps.newHashMap();
        //富文本中对于返回值有自自己的要求，我们使用的是simditor富文本插件，所以按照simditor的要求进行返回
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
    }

}
