package com.blotout.io;

import android.app.Application;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowApplication;

import java.io.File;

import static android.Manifest.permission.INTERNET;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by ankuradhikari on 16,November,2020
 */
public class BOTestUtils {

    public static Application mockApplication() {
        Application application = mock(Application.class);
        when(application.checkCallingOrSelfPermission(INTERNET)).thenReturn(PERMISSION_GRANTED);
        final File parent = RuntimeEnvironment.application.getFilesDir();
        doAnswer(
                new Answer() {
                    @Override
                    public Object answer(InvocationOnMock invocation) throws Throwable {
                        Object[] args = invocation.getArguments();
                        String fileName = (String) args[0];
                        return new File(parent, fileName);
                    }
                })
                .when(application)
                .getDir(anyString(), anyInt());
        doAnswer(
                new Answer() {
                    @Override
                    public Object answer(InvocationOnMock invocation) throws Throwable {
                        Object[] args = invocation.getArguments();
                        String name = (String) args[0];
                        int mode = (int) args[1];
                        return RuntimeEnvironment.application.getSharedPreferences(
                                name, mode);
                    }
                })
                .when(application)
                .getSharedPreferences(anyString(), anyInt());
        return application;
    }

    public static void grantPermission(final Application app, final String permission) {
        ShadowApplication shadowApp = Shadows.shadowOf(app);
        shadowApp.grantPermissions(permission);
    }

}
