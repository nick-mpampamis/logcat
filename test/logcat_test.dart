import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:logcat/logcat.dart';

void main() {
  const MethodChannel channel = MethodChannel('app.channel.logcat');

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return 'Simulated logcat';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('execute', () async {
    expect(await Logcat.execute(), 'Simulated logcat');
  });
}
