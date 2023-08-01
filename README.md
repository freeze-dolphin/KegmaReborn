# MysticWoods

示例项目：[MysticWoods by Quillraven](https://github.com/Quillraven/MysticWoods)

LibKTX 学习：[Youtube 上 Quillraven 的教程](https://www.youtube.com/playlist?list=PLTKHCDn5RKK-_mX0s8BJNz7pQecR25689)

曾经起过一个头头 [Keyma](https://github.com/freeze-dolphin/archives/blob/master/archived/repositories/Kegma.zip)，放弃了，这次重来

## 普通地图素材处理

为了防止 `Texture Bleeding`，需要使用 `tile-extruder` 处理素材

```shell
$ tile-extruder --tileWidth 32 --tileHeight 32 --input '原始素材文件位置'
```

然后导入 `Tiled`，注意选择 `边距 = 1`，`间距 = 2`

## 拐角地图素材处理

- 首先用 `tile-extruder` 处理素材

    ```shell
    $ tile-extruder --tileWidth 32 --tileHeight 32 --input '原始素材文件位置'
    ```

- 然后将素材使用 convert 指令切成 17x 单片（因为边缘处理后的素材比原素材大了一个像素）

    ```shell
    $ convert '边缘处理后的素材文件位置' -crop 17x17 ./_%02d.png
    ```

- 最烦的一步，筛选需要的拐角图块
  然后将其重命名为 `a (左上)`、`b (右上)`、`c (左下)`、`d (右下)`开头的文件

- 在拐角图块所在文件夹放置底面图块，命名为 `main.png`

- 执行转换程序

    ```shell
    $ python3 convert.py 'main.png 和拐角图块所在的目录'
    ```

- 将转换后的拐角图块拼接成大图

    ```shell
    $ montage *_0*.png -mode concatenate -tile 4x4 'combined.png'
    ```

- 清理不需要的文件，然后使用 `GDX Texture Packer` 打包所有地图素材

- 导入 `Tiled`，注意选择 `边距 = 1`，`间距 = 2`
