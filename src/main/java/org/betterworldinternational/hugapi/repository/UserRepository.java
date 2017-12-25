package org.betterworldinternational.hugapi.repository;

import org.betterworldinternational.hugapi.route.response.MapPin;
import org.betterworldinternational.hugapi.util.DBUtil;
import org.sql2o.Connection;

import java.util.List;

public class UserRepository {
    public int getEffectCount(int userId) {
        String query = "call recursive_lookup(:userId, :isEffect)";
        try (Connection conn = DBUtil.sql2o().open()) {
            return conn.createQuery(query)
                    .addParameter("userId", userId)
                    .addParameter("isEffect", 1)
                    .executeAndFetchFirst(Integer.class);
        }
    }

    public int getActivatesCount(int userId) {
        String query = "call recursive_lookup(:userId, :isEffect)";
        try (Connection conn = DBUtil.sql2o().open()) {
            return conn.createQuery(query)
                    .addParameter("userId", userId)
                    .addParameter("isEffect", 0)
                    .executeAndFetchFirst(Integer.class);
        }
    }

    public int getAllEffectCount() {
        String query = "select count(*) from users where completed = true";
        try (Connection conn = DBUtil.sql2o().open()) {
            return conn.createQuery(query)
                    .executeAndFetchFirst(Integer.class);
        }
    }

    public int getAllActivatesCount() {
        String query = "select count(*) from users";
        try (Connection conn = DBUtil.sql2o().open()) {
            return conn.createQuery(query)
                    .executeAndFetchFirst(Integer.class);
        }
    }

    public void addUser(String username, String email, String token, String inviteCode, Integer challengerId) {
        String query = "insert into users(username, email, token, inviteCode, challengerId) values(:username, :email, :token, :inviteCode, :challengerId)";
        try (Connection conn = DBUtil.sql2o().open()) {
            conn.createQuery(query)
                    .addParameter("username", username)
                    .addParameter("email", email)
                    .addParameter("token", token)
                    .addParameter("inviteCode", inviteCode)
                    .addParameter("challengerId", challengerId)
                    .executeUpdate();
        }
    }

    public void userDidIt(int userId, String image, double latitude, double longitude) {
        String query = "update users set completed=true, image=:image, latitude=:latitude,  longitude=:longitude where id=:userId";
        try (Connection conn = DBUtil.sql2o().open()) {
            conn.createQuery(query)
                    .addParameter("userId", userId)
                    .addParameter("latitude", latitude)
                    .addParameter("longitude", longitude)
                    .addParameter("image", image)
                    .executeUpdate();
        }
    }

    public List<MapPin> getMap() {
        String query = "select id as userId, image, latitude, longitude from users where completed=true";
        try (Connection conn = DBUtil.sql2o().open()) {
            return conn.createQuery(query)
                    .executeAndFetch(MapPin.class);
        }
    }

    public int getUserIdByEmail(String email) {
        String query = "select id from users where email=:email";
        try (Connection conn = DBUtil.sql2o().open()) {
            return conn.createQuery(query, true)
                    .addParameter("email", email)
                    .executeAndFetchFirst(Integer.class);
        } catch (Exception e) {
            return 0;
        }
    }

    public Integer getUserIdByInviteCode(String inviteCode) {
        String query = "select id from users where inviteCode=:inviteCode";
        try (Connection conn = DBUtil.sql2o().open()) {
            return conn.createQuery(query, true)
                    .addParameter("inviteCode", inviteCode)
                    .executeAndFetchFirst(Integer.class);
        } catch (Exception e) {
            return null;
        }
    }

    public int getUserIdByToken(String token) {
        String query = "select id from users where token=:token";
        try (Connection conn = DBUtil.sql2o().open()) {
            return conn.createQuery(query)
                    .addParameter("token", token)
                    .executeAndFetchFirst(Integer.class);
        } catch (Exception e) {
            return 0;
        }
    }
}