package com.shopme.admin.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.admin.GeneralSettingBag;
import com.shopme.admin.repository.SettingRepository;
import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;

@Service
public class SettingService {

	@Autowired
	private SettingRepository repo;

	public List<Setting> listAllSettings() {
		return (List<Setting>) repo.findAll();
	}

	public GeneralSettingBag getGeneralSetting() {
		List<Setting> settings = new ArrayList<>();

		List<Setting> generalSettings = repo.findByCategory(SettingCategory.GENERAL);
		List<Setting> currencySettings = repo.findByCategory(SettingCategory.CURRENCY);

		settings.addAll(generalSettings);
		settings.addAll(currencySettings);

		return new GeneralSettingBag(settings);
	}

	public List<Setting> getListGeneralSettings() {
		List<Setting> settings = new ArrayList<>();
		List<Setting> generalSettings = repo.findByCategory(SettingCategory.GENERAL);

		settings.addAll(generalSettings);
		return settings;
	}

	public void saveAll(Iterable<Setting> settings) {
		repo.saveAll(settings);
	}

	public List<Setting> getMailServerSettings() {
		return repo.findByCategory(SettingCategory.MAIL_SERVER);
	}

	public List<Setting> getMailTemplateSettings() {
		return repo.findByCategory(SettingCategory.MAIL_TEMPLATE);
	}
}
