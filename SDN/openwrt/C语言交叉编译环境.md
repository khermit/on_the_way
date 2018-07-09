## C语言交叉编译环境



1. 去官网https://archive.openwrt.org/chaos_calmer/15.05/ar71xx/generic/下载[OpenWrt-SDK-15.05-ar71xx-generic_gcc-4.8-linaro_uClibc-0.9.33.2.Linux-x86_64.tar.bz2](https://archive.openwrt.org/chaos_calmer/15.05/ar71xx/generic/OpenWrt-SDK-15.05-ar71xx-generic_gcc-4.8-linaro_uClibc-0.9.33.2.Linux-x86_64.tar.bz2)，并解压

2. 设置环境变量

   ```shell
   #openwrt gcc
   export PATH=$PATH:/home/***/OpenWrt-SDK-15.05-ar71xx-generic_gcc-4.8-linaro_uClibc-0.9.33.2.Linux-x86_64/staging_dir/toolchain-mips_34kc_gcc-4.8-linaro_uClibc-0.9.33.2/bin
   export export STAGING_DIR=/home/***/OpenWrt-SDK-15.05-ar71xx-generic_gcc-4.8-linaro_uClibc-0.9.33.2.Linux-x86_64/staging_dir
   ```

3. 编译源程序

   ```shell
   mips-openwrt-linux-uclibc-gcc test.c -o test
   ```

4. 将test拷贝到openwrt路由器中，./test直接运行即可

