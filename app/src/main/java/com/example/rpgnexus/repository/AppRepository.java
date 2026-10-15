package com.example.rpgnexus.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.rpgnexus.helpers.LocalDatabaseHelper;
import com.example.rpgnexus.models.CharacterSheet;
import com.example.rpgnexus.models.User;

import java.util.List;
import java.util.UUID;

public class AppRepository {
    private static final String PREFS_NAME = "rpgnexus_session";
    private static final String KEY_USER_ID = "logged_user_id";
    private static final String KEY_IS_GUEST = "is_guest";

    private static AppRepository instance;
    private final LocalDatabaseHelper dbHelper;
    private final SharedPreferences prefs;

    private User currentUser;
    private boolean isGuest;

    private AppRepository(Context context) {
        dbHelper = new LocalDatabaseHelper(context.getApplicationContext());
        prefs = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        isGuest = prefs.getBoolean(KEY_IS_GUEST, false);
        String savedUserId = prefs.getString(KEY_USER_ID, null);

        if (!isGuest && savedUserId != null) {
            try {
                List<User> users = dbHelper.getAllUsers();
                for (User u : users) {
                    if (u.getId().equals(savedUserId)) {
                        currentUser = u;
                        break;
                    }
                }
            } catch (Exception e) {
                currentUser = null;
            }
        }
    }

    public static synchronized AppRepository getInstance(Context context) {
        if (instance == null) {
            instance = new AppRepository(context);
        }
        return instance;
    }

    public boolean isGuest() {
        return isGuest || currentUser == null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void loginGuest() {
        this.isGuest = true;
        this.currentUser = null;
        prefs.edit().putBoolean(KEY_IS_GUEST, true).remove(KEY_USER_ID).apply();
    }

    public boolean login(String email, String password) {
        User user = dbHelper.authenticate(email, password);
        if (user != null) {
            if (user.isBlocked()) {
                return false; // Blocked user cannot log in
            }
            this.currentUser = user;
            this.isGuest = false;
            prefs.edit().putBoolean(KEY_IS_GUEST, false).putString(KEY_USER_ID, user.getId()).apply();
            return true;
        }
        return false;
    }

    public boolean register(String email, String password, String displayName) {
        String id = "user_" + UUID.randomUUID().toString();
        User newUser = new User(id, email, displayName, User.ROLE_USER, false);
        boolean success = dbHelper.insertUser(newUser, password);
        if (success) {
            this.currentUser = newUser;
            this.isGuest = false;
            prefs.edit().putBoolean(KEY_IS_GUEST, false).putString(KEY_USER_ID, newUser.getId()).apply();
        }
        return success;
    }

    public void logout() {
        this.currentUser = null;
        this.isGuest = true;
        prefs.edit().clear().apply();
    }

    public void resetDatabase() {
        dbHelper.resetDatabase();
        logout();
    }

    // Sheets
    public List<CharacterSheet> getPublicSheets(String query, String systemFilter, String genderFilter, String styleFilter) {
        return dbHelper.getPublicSheets(query, systemFilter, genderFilter, styleFilter);
    }

    public List<CharacterSheet> getUserSheets() {
        if (currentUser == null) return java.util.Collections.emptyList();
        return dbHelper.getUserSheets(currentUser.getId());
    }

    public List<CharacterSheet> getAllSheetsForAdmin() {
        return dbHelper.getAllSheetsForAdmin();
    }

    public boolean saveSheet(CharacterSheet sheet) {
        if (sheet.getId() == null || sheet.getId().trim().isEmpty()) {
            sheet.setId("sheet_" + UUID.randomUUID().toString());
        }
        return dbHelper.saveSheet(sheet);
    }

    public boolean deleteSheet(String sheetId) {
        return dbHelper.deleteSheet(sheetId);
    }

    // Admin Users Management
    public List<User> getAllUsers() {
        return dbHelper.getAllUsers();
    }

    public boolean toggleBlockUser(String userId, boolean currentStatus) {
        return dbHelper.updateUserBlocked(userId, !currentStatus);
    }

    public boolean deleteUser(String userId) {
        return dbHelper.deleteUser(userId);
    }
}
