package com.isansc.desafioconcrete;

import com.isansc.desafioconcrete.model.User;
import com.isansc.desafioconcrete.objects.UserTestObject;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * User convertion from JSON unit test, which will execute on the development machine (host).
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UserJsonUnitTest {

    @Test
    public void testConvertJsonToUser() {
        try {
            User user = UserTestObject.fromJSON(new JSONObject(UserTestObject.USER_JSON_STRING));
            assertEquals("Test failed compairing converted user's Id", UserTestObject.USER_ID, user.getId());
            assertEquals("Test failed compairing converted user's Login", UserTestObject.USER_LOGIN, user.getLogin());
            assertEquals("Test failed compairing converted user's Avatar URL", UserTestObject.USER_AVATAR_URL, user.getAvatarUrl());
            assertEquals("Test failed compairing converted user's Type", UserTestObject.USER_TYPE, user.getType());
        } catch (JSONException e) {
            fail("Test failed parsing from string to JSON");
        }
    }

    @Test
    public void testConvertUserToJson() {
        UserTestObject user = new UserTestObject();
        String convertedJsonUser = user.toJSON();

        assertEquals("Test failed compairing converted user's Type", UserTestObject.USER_JSON_STRING, convertedJsonUser);
    }
}