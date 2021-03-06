package com.project.dwine.manage.controller;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.dwine.manage.model.service.InventoryMgService;
import com.project.dwine.manage.model.vo.Inventory;
import com.project.dwine.notice.model.vo.Notice;
import com.project.dwine.paging.PageInfo;
import com.project.dwine.product.model.service.ProductService;
import com.project.dwine.product.model.vo.Product;



@Controller
@RequestMapping("/manage")
public class InventoryMgController {
	private InventoryMgService inventoryMgService;
	private ProductService productService;
	
	@Autowired
	public InventoryMgController(InventoryMgService inventoryMgService, ProductService productService ) {
		this.inventoryMgService = inventoryMgService;
		this.productService = productService;
	}
	
	//메인화면
	@GetMapping("/inventoryMg/main")
	public ModelAndView invenList(ModelAndView mv, @RequestParam(value="page", required=false) String page){
		int listCount = inventoryMgService.invenTotalListCnt();
	    int resultPage = 1;
	    
	    if(page != null) {
			resultPage = Integer.parseInt(page);
		}
		
		PageInfo pi = new PageInfo(resultPage, listCount, 10, 10);
		int startRow = (pi.getPage() - 1) * pi.getBoardLimit() + 1;
        int endRow = startRow + pi.getBoardLimit() - 1;
        
        Map<String, Object> inven = new HashMap<String, Object>();
		inven.put("startRow", startRow);
		inven.put("endRow", endRow);
	    
		List<Inventory> invenList = inventoryMgService.invenTotalList(inven);
		
		Inventory totalStock = inventoryMgService.selectTotalStock();
		Inventory totalShop = inventoryMgService.selectTotalShop();
		Inventory todayReceiving = inventoryMgService.selectTodayReceiving();
		mv.addObject("invenList", invenList);
		mv.addObject("totalStock", totalStock);
		mv.addObject("totalShop", totalShop);
		mv.addObject("todayReceiving", todayReceiving);
		mv.addObject("pi", pi);
		mv.setViewName("manage/inventoryMg/main");
		return mv;
	}
	
	//인벤토리 검색
	@PostMapping("/inventoryMg/main")
	@ResponseBody
	public Map<String, Object> seachMainInven(@RequestParam(value="page", required=false) String page, 
			@RequestParam(value="searchStandard", required=false) String searchStandard, 
			@RequestParam(value="searchValue", required=false) String searchValue, 
			@RequestParam(value="startDate", required=false) String startDate, 
			@RequestParam(value="endDate", required=false) String endDate) throws IOException {
 		
		Map<String, Object> inven = new HashMap<String, Object>();
		inven.put("searchStandard", searchStandard);
		inven.put("searchValue", searchValue);
		inven.put("startDate", startDate);
		inven.put("endDate", endDate);
		
		
 		int listCount = inventoryMgService.invenSearchListCnt(inven);
	    int resultPage = 1;
	    
	    if(page != null) {
			resultPage = Integer.parseInt(page);
		}
		
		PageInfo pi = new PageInfo(resultPage, listCount, 10, 10);
		int startRow = (pi.getPage() - 1) * pi.getBoardLimit() + 1;
		int endRow = startRow + pi.getBoardLimit() - 1;
		
		inven.put("startRow", startRow);
		inven.put("endRow", endRow);
		
		
 		List<Inventory> searchInvenList = inventoryMgService.searchInvenList(inven);
 		
		
		Inventory totalStock = inventoryMgService.selectTotalStock();
		Inventory totalShop = inventoryMgService.selectTotalShop();
		Inventory todayReceiving = inventoryMgService.selectTodayReceiving();
 		
 		Map<String, Object> map = new HashMap<>();
 		map.put("totalStock", totalStock);
 		map.put("totalShop", totalShop);
 		map.put("todayReceiving", todayReceiving);
 		map.put("pi", pi);
 		map.put("searchInvenList", searchInvenList);
 		
 		
 		return map;
 	}
	

	
	
	
	
	//입고등록
	@GetMapping("/inventoryMg/regist") 
	public ModelAndView registPage(ModelAndView mv) {
		
		Inventory totalStock = inventoryMgService.selectTotalStock();
		Inventory totalShop = inventoryMgService.selectTotalShop();
		Inventory todayReceiving = inventoryMgService.selectTodayReceiving();
		
		mv.addObject("totalStock", totalStock);
		mv.addObject("totalShop", totalShop);
		mv.addObject("todayReceiving", todayReceiving);
		
		mv.setViewName("manage/inventoryMg/regist");
		return mv;
	}
	
	//입고를 공지사항과 다르게 적용해보기
	@PostMapping("/inventoryMg/regist") //동일url이지만 전송버튼을 눌렀을때 하는 것. 
	public String registInven(Model model, HttpServletRequest request, RedirectAttributes rttr) {
	
	    int inven_count = Integer.parseInt(request.getParameter("inven_count"));
		int inven_cost = Integer.parseInt(request.getParameter("inven_cost"));
		int product_no = Integer.parseInt(request.getParameter("product_no"));
		
		Inventory inven = new Inventory(inven_count, inven_cost, product_no);
		int result = inventoryMgService.registInventory(inven);
		
		if(result > 1) {
			rttr.addFlashAttribute("message", "입고등록에 성공하였습니다.");
		} else {
			rttr.addFlashAttribute("message", "입고등록에 실패하였습니다. ");
		}
		return "redirect:main";
	}
	
	//날짜 오늘, 7일, 한달 SELECT
	//1
	
	
	
	//7
	
	
	
	//30
	
	
	
	//상품등록을 위한검색
	@PostMapping("/inventoryMg/search")
	@ResponseBody
	public List<Product> seachProduct(@RequestParam String searchStandard, @RequestParam String searchValue) throws IOException {
		List<Product> searchList = productService.searchProductList(searchStandard, searchValue);
		return searchList;
	}
	
	
	//상품no로 regist에 불러오기
	@PostMapping("/inventoryMg/searchByProductNo")
	@ResponseBody
	public Product selectProductByNo(@RequestParam int productNo) {
		return productService.selectProductByNo(productNo);
	}
	
	
	// 입고 주문 취소 cancle 
	@PostMapping("/inventoryMg/cancle")
	public String cancleInventory(RedirectAttributes rttr, @RequestParam int inven_no){
		int result =  inventoryMgService.cancleInventory(inven_no);
		
		if(result > 1) {
			rttr.addFlashAttribute("message", "입고취소가 성공하였습니다.");
		} else {
			rttr.addFlashAttribute("message", "입고취소가 실패하였습니다. ");
		}
	    
	    return "redirect:main";
	   }

	
	
	
	

}
