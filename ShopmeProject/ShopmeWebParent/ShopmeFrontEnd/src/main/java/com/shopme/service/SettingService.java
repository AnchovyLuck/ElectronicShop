package com.shopme.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.EmailSettingBag;
import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;
import com.shopme.repository.SettingRepository;

@Service
public class SettingService {

	@Autowired
	private SettingRepository repo;

	public List<Setting> getGeneralSettings() {
		return repo.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.CURRENCY);
	}
	
	public EmailSettingBag getEmailSettings() {
		List<Setting> settings = repo.findByCategory(SettingCategory.MAIL_SERVER);
		settings.addAll(repo.findByCategory(SettingCategory.MAIL_TEMPLATE));
		
		return new EmailSettingBag(settings);
 	}
}
