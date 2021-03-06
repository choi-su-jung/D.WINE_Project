package com.project.dwine.manage.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import com.project.dwine.manage.model.vo.Inventory;

@Mapper
public interface InventoryMgMapper {

	List<Inventory> selectInvenMgList();

	//검색
	int invenTotalListCnt();
	//List<Inventory> invenTotalList(int startRow, int endRow);
	List<Inventory> invenTotalList(Map<String, Object> inven);
	
	int invenSearchListCnt(Map<String, Object> inven);
	List<Inventory> searchInvenList(Map<String, Object> inven);
	
	/*
	 * int invenSearchListCnt(String searchStandard, String searchValue, String
	 * startDate, String endDate); List<Inventory> searchInvenList(String
	 * searchStandard, String searchValue, String startDate, String endDate, int
	 * startRow, int endRow);
	 */
	
	

	//카운트
	Inventory selectTotalStock();
	Inventory selectTotalShop();
	Inventory selectTotalReceiving();

	//등록취소
	int registInventory(Inventory inven);
	int cancleInventory(int inven_no);

	



	
}
