package com.library.service;

import com.library.model.LibrarySettings;
import com.library.repository.LibrarySettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LibrarySettingsService {
    
    @Autowired
    private LibrarySettingsRepository settingsRepository;
    
    public LibrarySettings getSettings() {
        List<LibrarySettings> settings = settingsRepository.findAll();
        if (settings.isEmpty()) {
            // Create default settings if none exist
            LibrarySettings defaultSettings = new LibrarySettings();
            return settingsRepository.save(defaultSettings);
        }
        return settings.get(0);
    }
    
    public LibrarySettings updateSettings(LibrarySettings settings) {
        LibrarySettings existingSettings = getSettings();
        existingSettings.setAllowedDays(settings.getAllowedDays());
        existingSettings.setFinePerDay(settings.getFinePerDay());
        existingSettings.setMaxBooksPerUser(settings.getMaxBooksPerUser());
        return settingsRepository.save(existingSettings);
    }
}
