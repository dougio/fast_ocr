快速 ocr
=======

这是一个借助百度文字识别端口，帮助大家快速进行屏幕文字 OCR 的软件。
[![Build Status](https://www.travis-ci.com/dougio/fast_ocr.svg?branch=master)](https://www.travis-ci.com/dougio/fast_ocr)

操作方法
=======

1. 截图
2. 启动程序
3. 粘贴到目的地

> 兼容性：Mac\Win，我没有试验过 Linux，应该会需要 GUI。

超快速试用命令如下

```bash
java -jar ocr-xxx.jar
```

以下命令可以把读取出来的内容用合成语音读出来
```bash
java -jar ocr-xxx.jar --read
```

以下命令可以只用语音读出内容，而不把剪贴版中的图片换成文字
```bash
java -jar ocr-xxx.jar --read --keepimg
```

以下命令可以开启不限量模式，但需要自己开[百度云账号](https://console.bce.baidu.com/ai/)，并付费。
```bash
java -jar -Dbaidu.appid={appid} \
 -Dbaidu.appkey={apkey} \
 -Dbaidu.secretkey={secretkey} \
 ./ocr-*****.jar
```

发生了什么
--------

1. 截取一张图片之后，内存里出现了一个画面，画面上有一些文字
2. 启动本程序，程序会识别出图片里的文字（需要联网，图像识别由百度云公共接口进行），并保存到剪贴板里
3. 将剪贴板内容粘贴到目的地

设计方案
======

* 为什么用 Java？

我开发的时候比较省事。

* 一定要安装 JRE 吗？

是的，因为我喜欢小 jar 包，不喜欢做封装。

* 常驻内存会减少启动时间，为什么不让程序常驻内存？

我没这个需求，我觉得每次启动花费三秒钟并不要紧，比常驻内存更节省内存。

* 我需要很多次 OCR，如何突破使用次数的限制？

可以自己去百度云申请一个 access key，启动时通过指定 
baidu.appid、baidu.appkey 和 baidu.secretkey 来代替默认账号。
这样就又多了500次免费机会了。当然可以向百度交钱，得到更多机会。

