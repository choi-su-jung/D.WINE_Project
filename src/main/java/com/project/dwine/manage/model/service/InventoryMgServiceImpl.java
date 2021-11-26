package com.project.dwine.manage.model.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dwine.manage.model.dao.InventoryMgMapper;
import com.project.dwine.manage.model.vo.Inventory;

@Service("InventoryMgService")
public class InventoryMgServiceImpl implements InventoryMgService {
	
	private final InventoryMgMapper inventoryMgMapper;
	
	@Autowired
	public InventoryMgServiceImpl(InventoryMgMapper inventoryMgMapper) {
		this.inventoryMgMapper =  inventoryMgMapper;
	}
	
	
	@Override
	public List<Inventory> selectInvenMgList() {
		return inventoryMgMapper.selectInvenMgList();
	}


	@Override
	public Inventory selectTotalStock() {
		return inventoryMgMapper.selectTotalStock();
	}


	@Override
	public Inventory selectTotalShop() {
		return inventoryMgMapper.selectTotalShop();
	}


	@Override
	public Inventory selectTodayReceiving() {
		return inventoryMgMapper.selectTotalReceiving();
	}

}