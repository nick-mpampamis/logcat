package dev.pharsh.logcat;

import androidx.annotation.NonNull;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * LogcatPlugin
 */
public class LogcatPlugin implements FlutterPlugin, MethodCallHandler {

    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private MethodChannel methodChannel;

    @Override
    public void onAttachedToEngine(@NonNull @NotNull FlutterPluginBinding binding) {
        methodChannel = new MethodChannel(binding.getBinaryMessenger(), "app.channel.logcat");

        methodChannel.setMethodCallHandler(this);
    }

    @Override
    public void onDetachedFromEngine(@NonNull @NotNull FlutterPluginBinding binding) {
        methodChannel.setMethodCallHandler(null);
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        if (call.method.equals("execLogcat")) {
            String logs = getLogs();

            if (logs != null) {
                result.success(logs);
            }
            else {
                result.error("UNAVAILABLE", "logs not available.", null);
            }
        }
        else {
            result.notImplemented();
        }
    }

    private String getLogs() {
        try {
            Process process = Runtime.getRuntime().exec("logcat -d");

            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                return bufferedReader.lines()
                                     .collect(Collectors.joining(System.lineSeparator()));
            }
        }
        catch (IOException e) {
            return "EXCEPTION: " + e.toString();
        }
    }
}
