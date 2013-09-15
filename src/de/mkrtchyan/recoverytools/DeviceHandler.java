package de.mkrtchyan.recoverytools;

/*
 * Copyright (c) 2013 Ashot Mkrtchyan
 * Permission is hereby granted, free of charge, to any person obtaining a copy 
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights 
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell 
 * copies of the Software, and to permit persons to whom the Software is 
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in 
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;

import java.io.File;

import de.mkrtchyan.utils.Common;
import de.mkrtchyan.utils.Downloader;


public class DeviceHandler {
    /*
     * This class content all device specified information to provide
     * all information for all other classes for example:
     * What kind of partition and where is the recovery partition in the
     * FileSystem
     */

    public String DEVICE_NAME = Build.DEVICE.toLowerCase();
    public String RecoveryPath = "";

    public File CWM_IMG;
    public File TWRP_IMG;

    private String CWM_VERSION = "";
    private String TWRP_VERSION = "";
    public String EXT = ".img";
    final public String CWM_URL = "http://dslnexus.nazuka.net/recoveries";
    final public String TWRP_URL = "http://dslnexus.nazuka.net/recoveries";
    public boolean KERNEL_TO = false;
    public boolean FLASH_OVER_RECOVERY = false;
    public boolean MTD = false;
    public boolean TWRP = false;
    public boolean OTHER = false;
    public boolean CWM = false;
    private boolean TWRP_OFFICIAL = false;
    private boolean CWM_OFFICIAL = false;
    private Context mContext;

    public File fflash, fdump, charger, chargermon, ric;

    public DeviceHandler(Context mContext) {

        this.mContext = mContext;

        setPredefinedOptions();

        RecoveryPath = getRecoveryPath();

        if (new File("/proc/mtd").exists() && RecoveryPath.equals(""))
            MTD = true;

        getVersion();
        getSupportedSystems();
        constructFile();
    }

    public DeviceHandler(Context mContext, String CustomDevice) {

        this.mContext = mContext;

        if (BuildConfig.DEBUG && !CustomDevice.equals(""))
            DEVICE_NAME = CustomDevice;

        setPredefinedOptions();

        RecoveryPath = getRecoveryPath();

        if (new File("/dev/mtd/").exists() && RecoveryPath.equals(""))
            MTD = true;

        getVersion();
        getSupportedSystems();
        constructFile();
    }

    private void setPredefinedOptions() {

        String BOARD = Build.BOARD.toLowerCase();
        String MODEL = Build.MODEL.toLowerCase();
//	Set DEVICE_NAME predefined options

//      Motorola DROID RAZR M
        if (DEVICE_NAME.equals("xt907"))
            DEVICE_NAME = "scorpion_mini";

//      ASUS PadFone
        if (DEVICE_NAME.equals("padfone"))
            DEVICE_NAME = "a66";

//      HTC Fireball
        if (DEVICE_NAME.equals("valentewx"))
            DEVICE_NAME = "fireball";

//      LG Optimus GX2
        if (BOARD.equals("p990"))
            DEVICE_NAME = "p990";

//      Motorola Atrix HD
        if (DEVICE_NAME.equals("mb886")
                || MODEL.equals("mb886"))
            DEVICE_NAME = "qinara";

//      LG Optimus G International
        if (BOARD.equals("geehrc"))
            DEVICE_NAME = "e975";

//      LG Optimus G
        if (BOARD.equals("geefhd"))
            DEVICE_NAME = "e988";

//      Motorola DROID4
        if (DEVICE_NAME.equals("cdma_maserati")
                || BOARD.equals("maserati"))
            DEVICE_NAME = "maserati";

//      LG Spectrum 4G (vs920)
        if (DEVICE_NAME.equals("d1lv")
                || BOARD.equals("d1lv"))
            DEVICE_NAME = "vs930";

//      Motorola Droid 2 WE
        if (DEVICE_NAME.equals("cdma_droid2we"))
            DEVICE_NAME = "droid2we";

//      OPPO Find 5
        if (DEVICE_NAME.equals("x909")
                || DEVICE_NAME.equals("x909t"))
            DEVICE_NAME = "find5";

//      Samsung Galaxy S +
        if (DEVICE_NAME.equals("gt-i9001")
                || BOARD.equals("gt-i9001")
                || MODEL.equals("gt-i9001"))
            DEVICE_NAME = "galaxysplus";

//      Samsung Galaxy Tab 7 Plus
        if (DEVICE_NAME.equals("gt-p6200"))
            DEVICE_NAME = "p6200";

//      Samsung Galaxy Note 8.0
        if (MODEL.equals("gt-n5110"))
            DEVICE_NAME = "konawifi";

//		Kindle Fire HD 7"
        if (DEVICE_NAME.equals("d01e"))
            DEVICE_NAME = "kfhd7";

        if (BOARD.equals("rk29sdk"))
            DEVICE_NAME = "rk29sdk";

//      HTC ONE GSM
        if (BOARD.equals("m7")
                || DEVICE_NAME.equals("m7")
                || DEVICE_NAME.equals("m7ul"))
            DEVICE_NAME = "m7";

        if (DEVICE_NAME.equals("m7spr"))
            DEVICE_NAME = "m7wls";

//      Samsung Galaxy S4 (i9505/jflte)
        if (DEVICE_NAME.equals("jflte"))
            DEVICE_NAME = "jfltexx";

//		Galaxy Note
        if (DEVICE_NAME.equals("gt-n7000")
                || DEVICE_NAME.equals("n7000")
                || DEVICE_NAME.equals("galaxynote")
                || DEVICE_NAME.equals("n7000")
                || BOARD.equals("gt-n7000")
                || BOARD.equals("n7000")
                || BOARD.equals("galaxynote")
                || BOARD.equals("N7000"))
            DEVICE_NAME = "n7000";

        if (DEVICE_NAME.equals("p4noterf")
                || MODEL.equals("gt-n8000"))
            DEVICE_NAME = "n8000";

//      Samsung Galaxy Note 10.1
        if (MODEL.equals("gt-n8013")
                || DEVICE_NAME.equals("p4notewifi"))
            DEVICE_NAME = "n8013";

//      Samsung Galaxy Tab 2
        if (BOARD.equals("piranha")
                || MODEL.equals("gt-p3113"))
            DEVICE_NAME = "p3113";

        if (DEVICE_NAME.equals("espressowifi")
                || MODEL.equals("gt-p3110"))
            DEVICE_NAME = "p3110";

//		Galaxy Note 2
        if (DEVICE_NAME.equals("n7100")
                || DEVICE_NAME.equals("n7100")
                || DEVICE_NAME.equals("gt-n7100")
                || BOARD.equals("t03g")
                || BOARD.equals("n7100")
                || BOARD.equals("gt-n7100"))
            DEVICE_NAME = "t03g";

//		Galaxy Note 2 LTE
        if (DEVICE_NAME.equals("t0ltexx")
                || DEVICE_NAME.equals("gt-n7105")
                || DEVICE_NAME.equals("t0ltedv")
                || DEVICE_NAME.equals("gt-n7105T")
                || DEVICE_NAME.equals("t0ltevl")
                || DEVICE_NAME.equals("sgh-I317m")
                || BOARD.equals("t0ltexx")
                || BOARD.equals("gt-n7105")
                || BOARD.equals("t0ltedv")
                || BOARD.equals("gt-n7105T")
                || BOARD.equals("t0ltevl")
                || BOARD.equals("sgh-i317m"))
            DEVICE_NAME = "t0lte";

        if (DEVICE_NAME.equals("sgh-i317")
                || BOARD.equals("t0lteatt")
                || BOARD.equals("sgh-i317"))
            DEVICE_NAME = "t0lteatt";

        if (DEVICE_NAME.equals("sgh-t889")
                || BOARD.equals("t0ltetmo")
                || BOARD.equals("sgh-t889"))
            DEVICE_NAME = "t0ltetmo";

        if (BOARD.equals("t0ltecan"))
            DEVICE_NAME = "t0ltecan";

//		Galaxy S3 (international)
        if (DEVICE_NAME.equals("gt-i9300")
                || DEVICE_NAME.equals("galaxy s3")
                || DEVICE_NAME.equals("galaxys3")
                || DEVICE_NAME.equals("m0")
                || DEVICE_NAME.equals("i9300")
                || BOARD.equals("gt-i9300")
                || BOARD.equals("m0")
                || BOARD.equals("i9300"))
            DEVICE_NAME = "i9300";

//		Galaxy S2
        if (DEVICE_NAME.equals("gt-i9100g")
                || DEVICE_NAME.equals("gt-i9100m")
                || DEVICE_NAME.equals("gt-i9100p")
                || DEVICE_NAME.equals("gt-i9100")
                || DEVICE_NAME.equals("galaxys2")
                || BOARD.equals("gt-i9100g")
                || BOARD.equals("gt-i9100m")
                || BOARD.equals("gt-I9100p")
                || BOARD.equals("gt-i9100")
                || BOARD.equals("galaxys2"))
            DEVICE_NAME = "galaxys2";

//		Galaxy S2 ATT
        if (DEVICE_NAME.equals("sgh-i777")
                || BOARD.equals("sgh-i777")
                || BOARD.equals("galaxys2att"))
            DEVICE_NAME = "galaxys2att";

//		Galaxy S2 LTE (skyrocket)
        if (DEVICE_NAME.equals("sgh-i727")
                || BOARD.equals("skyrocket")
                || BOARD.equals("sgh-i727"))
            DEVICE_NAME = "skyrocket";

//      Galaxy S (i9000)
        if (DEVICE_NAME.equals("galaxys")
                || DEVICE_NAME.equals("galaxysmtd")
                || DEVICE_NAME.equals("gt-i9000")
                || DEVICE_NAME.equals("gt-i9000m")
                || DEVICE_NAME.equals("gt-i9000t")
                || BOARD.equals("galaxys")
                || BOARD.equals("galaxysmtd")
                || BOARD.equals("gt-i9000")
                || BOARD.equals("gt-i9000m")
                || BOARD.equals("gt-i9000t")
                || MODEL.equals("gt-i9000t")
                || DEVICE_NAME.equals("sph-d710bst")
                || MODEL.equals("sph-d710bst"))
            DEVICE_NAME = "galaxys";

//      Samsung Galaxy Note
        if (DEVICE_NAME.equals("gt-n7000b"))
            DEVICE_NAME = "n7000";

//		GalaxyS Captivate (SGH-I897)
        if (DEVICE_NAME.equals("sgh-i897")) {
            DEVICE_NAME = ("captivate");
        }

        if (BOARD.equals("gee")) {
            DEVICE_NAME = "geeb";
        }

//		Sony Xperia Z (C6603)
        if (DEVICE_NAME.equals("c6603")
                || DEVICE_NAME.equals("yuga")) {
            DEVICE_NAME = "c6603";
        }

        if (DEVICE_NAME.equals("c6603")
                || DEVICE_NAME.equals("c6602"))
            EXT = ".tar";

//		Sony Xperia S
        if (DEVICE_NAME.equals("lt26i"))
            DEVICE_NAME = "nozomi";

//		Sony Xperia T
        if (DEVICE_NAME.equals("lt30p"))
            DEVICE_NAME = "mint";

//      HTC Desire HD
        if (BOARD.equals("ace"))
            DEVICE_NAME = "ace";

//      Motorola Droid X
        if (DEVICE_NAME.equals("cdma_shadow")
                || BOARD.equals("shadow")
                || MODEL.equals("droidx"))
            DEVICE_NAME = "shadow";

//      LG Optimus L9
        if (DEVICE_NAME.equals("u2")
                || BOARD.equals("u2")
                || MODEL.equals("lg-p760"))
            DEVICE_NAME = "p760";

//      LG Optimus L5
        if (DEVICE_NAME.equals("m4")
                || MODEL.equals("lg-e610"))
            DEVICE_NAME = "e610";

//      Huawei U9508
        if (BOARD.equals("u9508")
                || DEVICE_NAME.equals("hwu9508"))
            DEVICE_NAME = "u9508";

//      Huawei Ascend P1
        if (DEVICE_NAME.equals("hwu9200")
                || BOARD.equals("u9200")
                || MODEL.equals("u9200"))
            DEVICE_NAME = "u9200";

//      Motorola RAZR
        if (DEVICE_NAME.equals("cdma_yangtze")
                || BOARD.equals("yangtze"))
            DEVICE_NAME = "yangtze";

//      Motorola Droid RAZR
        if (DEVICE_NAME.equals("cdma_spyder")
                || BOARD.equals("spyder"))
            DEVICE_NAME = "spyder";

//      Huawei M835
        if (DEVICE_NAME.equals("hwm835")
                || BOARD.equals("m835"))
            DEVICE_NAME = "m835";

//      LG Optimus Black
        if (DEVICE_NAME.equals("bproj_cis-xxx")
                || BOARD.equals("bproj")
                || MODEL.equals("lg-p970"))
            DEVICE_NAME = "p970";

//      LG Optimus X2
        if (DEVICE_NAME.equals("star"))
            DEVICE_NAME = "p990";

        if (DEVICE_NAME.equals("droid2")
                || DEVICE_NAME.equals("daytona")
                || DEVICE_NAME.equals("captivate")
                || DEVICE_NAME.equals("galaxys")
                || DEVICE_NAME.equals("galaxys2att")
                || DEVICE_NAME.equals("galaxys2")
                || DEVICE_NAME.equals("n7000")
                || DEVICE_NAME.equals("droid2we"))
            FLASH_OVER_RECOVERY = true;


//		Devices who kernel will be flashed to
        if (DEVICE_NAME.equals("nozomi")
                || DEVICE_NAME.equals("mint"))
            KERNEL_TO = true;

        if (FLASH_OVER_RECOVERY)
            EXT = ".zip";
    }

    private String getRecoveryPath() {

//      ASUS DEVICEs + Same
        if (DEVICE_NAME.equals("a66"))
            return "/dev/block/mmcblk0p15";

//		Nexus DEVICEs + Same
        if (DEVICE_NAME.equals("maguro")
                || DEVICE_NAME.equals("toro")
                || DEVICE_NAME.equals("toroplus"))
            return "/dev/block/platform/omap/omap_hsmmc.0/by-name/recovery";

        if (DEVICE_NAME.equals("grouper")
                || DEVICE_NAME.equals("tilapia")
                || DEVICE_NAME.equals("p880")
                || DEVICE_NAME.equals("n710"))
            return "/dev/block/platform/sdhci-tegra.3/by-name/SOS";

        if (DEVICE_NAME.equals("mako")
                || DEVICE_NAME.equals("geeb")
                || DEVICE_NAME.equals("vanquish")
                || DEVICE_NAME.equals("find5")
                || DEVICE_NAME.equals("jgedlte")
                || DEVICE_NAME.equals("flo")
                || DEVICE_NAME.equals("deb")
                || DEVICE_NAME.equals("scorpion_mini"))
            return "/dev/block/platform/msm_sdcc.1/by-name/recovery";

        if (DEVICE_NAME.equals("manta"))
            return "/dev/block/platform/dw_mmc.0/by-name/recovery";

//		Samsung DEVICEs + Same
        if (DEVICE_NAME.equals("d2att")
                || DEVICE_NAME.equals("d2tmo")
                || DEVICE_NAME.equals("d2mtr")
                || DEVICE_NAME.equals("d2vzw")
                || DEVICE_NAME.equals("d2spr")
                || DEVICE_NAME.equals("d2usc")
                || DEVICE_NAME.equals("d2can")
                || DEVICE_NAME.equals("sch-i929")
                || DEVICE_NAME.equals("e6710")
                || DEVICE_NAME.equals("expresslte")
                || DEVICE_NAME.equals("goghcri"))
            return "/dev/block/mmcblk0p18";

        if (DEVICE_NAME.equals("i9300")
                || DEVICE_NAME.equals("galaxys2")
                || DEVICE_NAME.equals("n8013")
                || DEVICE_NAME.equals("p3113")
                || DEVICE_NAME.equals("p3110")
                || DEVICE_NAME.equals("p6200")
                || DEVICE_NAME.equals("n8000")
                || DEVICE_NAME.equals("sph-d710vmub")
                || DEVICE_NAME.equals("p920")
                || DEVICE_NAME.equals("konawifi")
                || DEVICE_NAME.equals("t03gctc")
                || DEVICE_NAME.equals("cosmopolitan"))
            return "/dev/block/mmcblk0p6";

        if (DEVICE_NAME.equals("t03g")
                || DEVICE_NAME.equals("tf700t")
                || DEVICE_NAME.equals("tf201")
                || DEVICE_NAME.equals("t0lte")
                || DEVICE_NAME.equals("t0ltecan")
                || DEVICE_NAME.equals("t0ltektt")
                || DEVICE_NAME.equals("t0lteskt")
                || DEVICE_NAME.equals("t0ltespr")
                || DEVICE_NAME.equals("t0lteusc")
                || DEVICE_NAME.equals("t0ltevzw")
                || DEVICE_NAME.equals("t0lteatt")
                || DEVICE_NAME.equals("t0ltetmo")
                || DEVICE_NAME.equals("m3"))
            return "/dev/block/mmcblk0p9";

        if (DEVICE_NAME.equals("golden")
                || DEVICE_NAME.equals("villec2")
                || DEVICE_NAME.equals("vivo")
                || DEVICE_NAME.equals("vivow")
                || DEVICE_NAME.equals("kingdom")
                || DEVICE_NAME.equals("vision")
                || DEVICE_NAME.equals("mystul")
                || DEVICE_NAME.equals("jfltespr")
                || DEVICE_NAME.equals("jflteatt")
                || DEVICE_NAME.equals("jfltevzw")
                || DEVICE_NAME.equals("jfltexx")
                || DEVICE_NAME.equals("jfltecan")
                || DEVICE_NAME.equals("jfltetmo")
                || DEVICE_NAME.equals("jflteusc")
                || DEVICE_NAME.equals("flyer")
                || DEVICE_NAME.equals("saga")
                || DEVICE_NAME.equals("shooteru")
                || DEVICE_NAME.equals("golfu")
                || DEVICE_NAME.equals("glacier")
                || DEVICE_NAME.equals("runnymede"))
            return "/dev/block/mmcblk0p21";

        if (DEVICE_NAME.equals("jena"))
            return "/dev/block/mmcblk0p12";

        if (DEVICE_NAME.equals("GT-I9103")
                || DEVICE_NAME.equals("mevlana"))
            return "/dev/block/mmcblk0p8";

//      LG DEVICEs + Same
        if (DEVICE_NAME.equals("e610"))
            return "/dev/block/mmcblk0p17";

        if (DEVICE_NAME.equals("vs930")
                || DEVICE_NAME.equals("l0")
                || DEVICE_NAME.equals("ca201l")
                || DEVICE_NAME.equals("ef49k"))
            return "/dev/block/mmcblk0p19";

//		HTC DEVICEs + Same
        if (DEVICE_NAME.equals("holiday")
                || DEVICE_NAME.equals("vigor")
                || DEVICE_NAME.equals("a68"))
            return "/dev/block/mmcblk0p23";

        if (DEVICE_NAME.equals("m7")
                || DEVICE_NAME.equals("obakem")
                || DEVICE_NAME.equals("ovation"))
            return "/dev/block/mmcblk0p34";

        if (DEVICE_NAME.equals("m7wls"))
            return "/dev/block/mmcblk0p36";

        if (DEVICE_NAME.equals("endeavoru")
                || DEVICE_NAME.equals("enrc2b")
                || DEVICE_NAME.equals("p999")
                || DEVICE_NAME.equals("us9230e1"))
            return "/dev/block/mmcblk0p5";

        if (DEVICE_NAME.equals("ace")
                || DEVICE_NAME.equals("primou"))
            return "/dev/block/platform/msm_sdcc.2/mmcblk0p21";

        if (DEVICE_NAME.equals("pyramid"))
            return "/dev/block/platform/msm_sdcc.1/mmcblk0p21";

        if (DEVICE_NAME.equals("ville")
                || DEVICE_NAME.equals("evita")
                || DEVICE_NAME.equals("skyrocket")
                || DEVICE_NAME.equals("fireball")
                || DEVICE_NAME.equals("jewel")
                || DEVICE_NAME.equals("shooter"))
            return "/dev/block/mmcblk0p22";

        if (DEVICE_NAME.equals("dlxub1")
                || DEVICE_NAME.equals("dlx")
                || DEVICE_NAME.equals("dlxj")
                || DEVICE_NAME.equals("im-a840sp")
                || DEVICE_NAME.equals("im-a840s"))
            return "/dev/block/mmcblk0p20";

//		Motorola DEVICEs + Same
        if (DEVICE_NAME.equals("qinara")
                || DEVICE_NAME.equals("f02e"))
            return "/dev/block/mmcblk0p32";

        if (DEVICE_NAME.equals("e975")
                || DEVICE_NAME.equals("e988"))
            return "/dev/block/mmcblk0p28";

        if (DEVICE_NAME.equals("shadow")
                || DEVICE_NAME.equals("edison"))
            return "/dev/block/mmcblk1p16";

        if (DEVICE_NAME.equals("spyder")
                || DEVICE_NAME.equals("maserati"))
            return "/dev/block/mmcblk1p15";

        if (DEVICE_NAME.equals("olympus")
                || DEVICE_NAME.equals("ja3g")
                || DEVICE_NAME.equals("ja3gchnduos")
                || DEVICE_NAME.equals("daytona")
                || DEVICE_NAME.equals("konalteatt")
                || DEVICE_NAME.equals("lc1810")
                || DEVICE_NAME.equals("lt02wifi"))
            return "/dev/block/mmcblk0p10";

        if (DEVICE_NAME.equals("stingray")
                || DEVICE_NAME.equals("wingray")
                || DEVICE_NAME.equals("everest"))
            return "/dev/block/platform/sdhci-tegra.3/by-name/recovery";

//      Huawei DEVICEs + Same
        if (DEVICE_NAME.equals("u9508"))
            return "/dev/block/platform/hi_mci.1/by-name/recovery";

        if (DEVICE_NAME.equals("u9200")
                || DEVICE_NAME.equals("kfdh7"))
            return "/dev/block/platform/omap/omap_hsmmc.1/by-name/recovery";

//		Sony DEVICEs + Same
        if (DEVICE_NAME.equals("nozomi"))
            return "/dev/block/mmcblk0p3";

        if (DEVICE_NAME.equals("c6603"))
            return "/system/bin/recovery.tar";

//		LG DEVICEs + Same
        if (DEVICE_NAME.equals("p990")
                || DEVICE_NAME.equals("tf300t"))
            return "/dev/block/mmcblk0p7";

        if (DEVICE_NAME.equals("x3")
                || DEVICE_NAME.equals("picasso")
                || DEVICE_NAME.equals("picasso_m")
                || DEVICE_NAME.equals("enterprise_ru"))
            return "/dev/block/mmcblk0p1";

        if (DEVICE_NAME.equals("m3s")
                || DEVICE_NAME.equals("bryce"))
            return "/dev/block/mmcblk0p14";

        if (DEVICE_NAME.equals("p970")
                || DEVICE_NAME.equals("mint")
                || DEVICE_NAME.equals("u2")
                || DEVICE_NAME.equals("p760")
                || DEVICE_NAME.equals("p768"))
            return "/dev/block/mmcblk0p4";

//		ZTE DEVICEs + Same
        if (DEVICE_NAME.equals("warp2")
                || DEVICE_NAME.equals("hwc8813")
                || DEVICE_NAME.equals("galaxysplus")
                || DEVICE_NAME.equals("cayman")
                || DEVICE_NAME.equals("ancora_tmo")
                || DEVICE_NAME.equals("c8812e")
                || DEVICE_NAME.equals("batman_skt"))
            return "/dev/block/mmcblk0p13";

        if (DEVICE_NAME.equals("elden"))
            return "/dev/block/mmcblk0p16";

        return "";
    }

    public void getSupportedSystems() {

        if (CWM_OFFICIAL
                || DEVICE_NAME.equals("c6602")
                || DEVICE_NAME.equals("c6603")
                || DEVICE_NAME.equals("jena"))
            CWM = true;

        if (TWRP_OFFICIAL
                || DEVICE_NAME.equals("c6603")
                || DEVICE_NAME.equals("jena")
                || DEVICE_NAME.equals("kfhd7"))
            TWRP = true;

        if (!RecoveryPath.equals("")
                || FLASH_OVER_RECOVERY
                || MTD
                || CWM
                || TWRP)
            OTHER = true;
    }

    public void getVersion() {
//		CLOCKWORKMOD touch supported devices
        if (DEVICE_NAME.equals("ace")
                || DEVICE_NAME.equals("crespo")
                || DEVICE_NAME.equals("crespo4g")
                || DEVICE_NAME.equals("d2att")
                || DEVICE_NAME.equals("d2tmo")
                || DEVICE_NAME.equals("endeavoru")
                || DEVICE_NAME.equals("evita")
                || DEVICE_NAME.equals("fireball")
                || DEVICE_NAME.equals("galaxys2")
                || DEVICE_NAME.equals("golden")
                || DEVICE_NAME.equals("grouper")
                || DEVICE_NAME.equals("i9300")
                || DEVICE_NAME.equals("maguro")
                || DEVICE_NAME.equals("mako")
                || DEVICE_NAME.equals("manta")
                || DEVICE_NAME.equals("t03g")
                || DEVICE_NAME.equals("pyramid")
                || DEVICE_NAME.equals("saga")
                || DEVICE_NAME.equals("skyrocket")
                || DEVICE_NAME.equals("tilapia")
                || DEVICE_NAME.equals("toro")
                || DEVICE_NAME.equals("toroplus")
                || DEVICE_NAME.equals("ville")
                || DEVICE_NAME.equals("warp2")
                || DEVICE_NAME.equals("p990")
                || DEVICE_NAME.equals("tf700t")
                || DEVICE_NAME.equals("dlx")
                || DEVICE_NAME.equals("jflte")
                || DEVICE_NAME.equals("d2spr")
                || DEVICE_NAME.equals("supersonic")
                || DEVICE_NAME.equals("olympus")
                || DEVICE_NAME.equals("m7spr")
                || DEVICE_NAME.equals("jfltespr")
                || DEVICE_NAME.equals("jewel")
                || DEVICE_NAME.equals("shooter")
                || DEVICE_NAME.equals("jfltevzw")
                || DEVICE_NAME.equals("p970")
                || DEVICE_NAME.equals("p760")
                || DEVICE_NAME.equals("jfltecan")
                || DEVICE_NAME.equals("jfltexx")
                || DEVICE_NAME.equals("jfltespr")
                || DEVICE_NAME.equals("m7")
                || DEVICE_NAME.equals("m7wls")
                || DEVICE_NAME.equals("jfltevzw")
                || DEVICE_NAME.equals("p880")
                || DEVICE_NAME.equals("n8013")
                || DEVICE_NAME.equals("jfltetmo")
                || DEVICE_NAME.equals("p3113")
                || DEVICE_NAME.equals("d2usc")
                || DEVICE_NAME.equals("bravo")
                || DEVICE_NAME.equals("find5")
                || DEVICE_NAME.equals("jflteatt")
                || DEVICE_NAME.equals("jflteusc")
                || DEVICE_NAME.equals("p3110")
                || DEVICE_NAME.equals("stingray")
                || DEVICE_NAME.equals("wingray")
                || DEVICE_NAME.equals("dlxj")
                || DEVICE_NAME.equals("n8000")
                || DEVICE_NAME.equals("p920")
                || DEVICE_NAME.equals("t0lte")
                || DEVICE_NAME.equals("shooteru")
                || DEVICE_NAME.equals("e975")
                || DEVICE_NAME.equals("glacier")
                || DEVICE_NAME.equals("vigor")
                || DEVICE_NAME.equals("runnymede")
                || DEVICE_NAME.equals("flo"))
            CWM_VERSION = "-touch";

//	    Newest Clockworkmod version for devices
        if (DEVICE_NAME.equals("sholes"))
            CWM_VERSION = CWM_VERSION + "-2.5.0.1";

        if (DEVICE_NAME.equals("heroc"))
            CWM_VERSION = CWM_VERSION + "-2.5.0.7";

        if (DEVICE_NAME.equals("sgh-i897")
                || DEVICE_NAME.equals("galaxys")
                || DEVICE_NAME.equals("captivate"))
            CWM_VERSION = CWM_VERSION + "-2.5.1.2";

        if (DEVICE_NAME.equals("leo"))
            CWM_VERSION = CWM_VERSION + "-3.1.0.0";

        if (DEVICE_NAME.equals("droid2")
                || DEVICE_NAME.equals("vivo")
                || DEVICE_NAME.equals("vivow")
                || DEVICE_NAME.equals("blade")
                || DEVICE_NAME.equals("shadow")
                || DEVICE_NAME.equals("p999"))
            CWM_VERSION = CWM_VERSION + "-5.0.2.0";

        if (DEVICE_NAME.equals("droid2we"))
            CWM_VERSION = CWM_VERSION + "-5.0.2.3";

        if (DEVICE_NAME.equals("daytona"))
            CWM_VERSION = CWM_VERSION + "-5.0.2.5";

        if (DEVICE_NAME.equals("thunderg")
                || DEVICE_NAME.equals("holiday"))
            CWM_VERSION = CWM_VERSION + "-5.0.2.7";

        if (DEVICE_NAME.equals("pico"))
            CWM_VERSION = CWM_VERSION + "-5.0.2.8";

        if (DEVICE_NAME.equals("supersonic"))
            CWM_VERSION = CWM_VERSION + "-5.8.0.1";

        if (DEVICE_NAME.equals("shooter")
                || DEVICE_NAME.equals("bravo")
                || DEVICE_NAME.equals("shooteru"))
            CWM_VERSION = CWM_VERSION + "-5.8.0.2";
        if (DEVICE_NAME.equals("pyramid"))
            CWM_VERSION = CWM_VERSION + "-5.8.0.9";

        if (DEVICE_NAME.equals("glacier"))
            CWM_VERSION = CWM_VERSION + "-5.8.1.0";

        if (DEVICE_NAME.equals("ace")
                || DEVICE_NAME.equals("saga")
                || DEVICE_NAME.equals("galaxys2")
                || DEVICE_NAME.equals("olympus"))
            CWM_VERSION = CWM_VERSION + "-5.8.1.5";

        if (DEVICE_NAME.equals("tf201"))
            CWM_VERSION = CWM_VERSION + "-5.8.3.4";

        if (DEVICE_NAME.equals("jewel"))
            CWM_VERSION = CWM_VERSION + "-5.8.3.5";

        if (DEVICE_NAME.equals("endeavoru"))
            CWM_VERSION = CWM_VERSION + "-5.8.4.0";

        if (DEVICE_NAME.equals("primou"))
            CWM_VERSION = CWM_VERSION + "-5.8.4.5";

        if (DEVICE_NAME.equals("Dummy"))
            CWM_VERSION = CWM_VERSION + "-6.0.1.1";

        if (DEVICE_NAME.equals("n7000"))
            CWM_VERSION = CWM_VERSION + "-6.0.1.2";

        if (DEVICE_NAME.equals("p970"))
            CWM_VERSION = CWM_VERSION + "-6.0.1.4";

        if (DEVICE_NAME.equals("p920"))
            CWM_VERSION = CWM_VERSION + "-6.0.1.9";

        if (DEVICE_NAME.equals("p3113"))
            CWM_VERSION = CWM_VERSION + "-6.0.2.3";

        if (DEVICE_NAME.equals("golden")
                || DEVICE_NAME.equals("warp2")
                || DEVICE_NAME.equals("p3110")
                || DEVICE_NAME.equals("runnymede"))
            CWM_VERSION = CWM_VERSION + "-6.0.2.7";

        if (DEVICE_NAME.equals("e610"))
            CWM_VERSION = CWM_VERSION + "-6.0.2.8";

        if (DEVICE_NAME.equals("t0ltevzw")
                || DEVICE_NAME.equals("e975"))
            CWM_VERSION = "-6.0.3.0";

        if (DEVICE_NAME.equals("crespo")
                || DEVICE_NAME.equals("crespo4g")
                || DEVICE_NAME.equals("d2att")
                || DEVICE_NAME.equals("d2tmo")
                || DEVICE_NAME.equals("dlxub1")
                || DEVICE_NAME.equals("evita")
                || DEVICE_NAME.equals("fireball")
                || DEVICE_NAME.equals("grouper")
                || DEVICE_NAME.equals("i9300")
                || DEVICE_NAME.equals("maguro")
                || DEVICE_NAME.equals("mako")
                || DEVICE_NAME.equals("manta")
                || DEVICE_NAME.equals("t03g")
                || DEVICE_NAME.equals("skyrocket")
                || DEVICE_NAME.equals("tilapia")
                || DEVICE_NAME.equals("toro")
                || DEVICE_NAME.equals("toroplus")
                || DEVICE_NAME.equals("ville")
                || DEVICE_NAME.equals("p990")
                || DEVICE_NAME.equals("tf700t")
                || DEVICE_NAME.equals("m7")
                || DEVICE_NAME.equals("dlx")
                || DEVICE_NAME.equals("d2spr")
                || DEVICE_NAME.equals("p760")
                || DEVICE_NAME.equals("p880")
                || DEVICE_NAME.equals("n8013")
                || DEVICE_NAME.equals("d2usc")
                || DEVICE_NAME.equals("find5")
                || DEVICE_NAME.equals("t0ltespr")
                || DEVICE_NAME.equals("t0lteatt")
                || DEVICE_NAME.equals("t0ltetmo")
                || DEVICE_NAME.equals("stingray")
                || DEVICE_NAME.equals("wingray")
                || DEVICE_NAME.equals("dlxj")
                || DEVICE_NAME.equals("vigor"))
            CWM_VERSION = CWM_VERSION + "-6.0.3.1";

        if (DEVICE_NAME.equals("jfltexx")
                || DEVICE_NAME.equals("jfltespr")
                || DEVICE_NAME.equals("m7wls")
                || DEVICE_NAME.equals("jfltevzw"))
            CWM_VERSION = CWM_VERSION + "-6.0.3.2";

        if (DEVICE_NAME.equals("n8000")
                || DEVICE_NAME.equals("t0lte"))
            CWM_VERSION = CWM_VERSION + "-6.0.3.6";

        if (DEVICE_NAME.equals("flo"))
            CWM_VERSION = CWM_VERSION + "-6.0.3.7";

        if (!CWM_VERSION.equals(""))
            CWM_OFFICIAL = true;

        if (DEVICE_NAME.equals("thunderg"))
            TWRP_VERSION = "-2.0.0alpha1";

        if (DEVICE_NAME.equals("leo"))
            TWRP_VERSION = "-2.2.0";

        if (DEVICE_NAME.equals("pico"))
            TWRP_VERSION = "-2.4.4.0";

        if (DEVICE_NAME.equals("passion")
                || DEVICE_NAME.equals("crespo")
                || DEVICE_NAME.equals("crespo4g")
                || DEVICE_NAME.equals("maguro")
                || DEVICE_NAME.equals("toro")
                || DEVICE_NAME.equals("toroplus")
                || DEVICE_NAME.equals("grouper")
                || DEVICE_NAME.equals("tilapia")
                || DEVICE_NAME.equals("mako")
                || DEVICE_NAME.equals("manta")
                || DEVICE_NAME.equals("tf700t")
                || DEVICE_NAME.equals("ace")
                || DEVICE_NAME.equals("saga")
                || DEVICE_NAME.equals("pyramid")
                || DEVICE_NAME.equals("fireball")
                || DEVICE_NAME.equals("vivo")
                || DEVICE_NAME.equals("vivow")
                || DEVICE_NAME.equals("supersonic")
                || DEVICE_NAME.equals("jewel")
                || DEVICE_NAME.equals("primou")
                || DEVICE_NAME.equals("ville")
                || DEVICE_NAME.equals("villec2")
                || DEVICE_NAME.equals("endeavoru")
                || DEVICE_NAME.equals("evita")
                || DEVICE_NAME.equals("dlxub1")
                || DEVICE_NAME.equals("dlx")
                || DEVICE_NAME.equals("m7")
                || DEVICE_NAME.equals("m7wls")
                || DEVICE_NAME.equals("olympus")
                || DEVICE_NAME.equals("find5")
                || DEVICE_NAME.equals("skyrocket")
                || DEVICE_NAME.equals("n8013")
                || DEVICE_NAME.equals("d2att")
                || DEVICE_NAME.equals("d2tmo")
                || DEVICE_NAME.equals("d2vzw")
                || DEVICE_NAME.equals("d2spr")
                || DEVICE_NAME.equals("d2usc")
                || DEVICE_NAME.equals("golden")
                || DEVICE_NAME.equals("geeb")
                || DEVICE_NAME.equals("p970")
                || DEVICE_NAME.equals("nozomi")
                || DEVICE_NAME.equals("mint")
                || DEVICE_NAME.equals("enrc2b")
                || DEVICE_NAME.equals("shooter")
                || DEVICE_NAME.equals("i9300")
                || DEVICE_NAME.equals("jgedlte")
                || DEVICE_NAME.equals("flo")
                || DEVICE_NAME.equals("deb")
                || DEVICE_NAME.equals("p999"))
            TWRP_VERSION = "-2.6.0.0";

        if (DEVICE_NAME.equals("t03g")
                || DEVICE_NAME.equals("t0lteusc")
                || DEVICE_NAME.equals("t0ltecan")
                || DEVICE_NAME.equals("t0ltektt")
                || DEVICE_NAME.equals("t0lteskt")
                || DEVICE_NAME.equals("t0ltespr")
                || DEVICE_NAME.equals("t0ltevzw"))
            TWRP_VERSION = "-2.6.0.1";

        if (DEVICE_NAME.equals("t0lteatt")
                || DEVICE_NAME.equals("t0ltetmo"))
            TWRP_VERSION = "-2.6.0.3";

        if (DEVICE_NAME.equals("n8000")
                || DEVICE_NAME.equals("golfu")
                || DEVICE_NAME.equals("heroc")
                || DEVICE_NAME.equals("t0lte")
                || DEVICE_NAME.equals("d2mtr")
                || DEVICE_NAME.equals("shooteru")
                || DEVICE_NAME.equals("holiday")
                || DEVICE_NAME.equals("a68"))
            TWRP_VERSION = "-2.6.1.0";

        if (DEVICE_NAME.equals("picasso")
                || DEVICE_NAME.equals("d2can")
                || DEVICE_NAME.equals("p990")
                || DEVICE_NAME.equals("runnymede")
                || DEVICE_NAME.equals("a66"))
            TWRP_VERSION = "-2.6.3.0";

        if (DEVICE_NAME.equals("tf201")) {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.ICE_CREAM_SANDWICH
                    || Build.VERSION.SDK_INT == Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                TWRP_VERSION = TWRP_VERSION + "-ICS";
            } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                TWRP_VERSION = TWRP_VERSION + "-JB";
            }
        }
        if (!TWRP_VERSION.equals(""))
            TWRP_OFFICIAL = true;
    }

    public void constructFile() {
        if (CWM_OFFICIAL) {
            CWM_IMG = new File(RecoveryTools.PathToRecoveries, "recovery-clockwork" + CWM_VERSION + "-" + DEVICE_NAME + EXT);
        } else {
            CWM_IMG = new File(RecoveryTools.PathToRecoveries, DEVICE_NAME + "-cwm" + EXT);
        }

        if (TWRP_OFFICIAL) {
            TWRP_IMG = new File(RecoveryTools.PathToRecoveries, "openrecovery-twrp" + TWRP_VERSION + "-" + DEVICE_NAME + EXT);
        } else {
            TWRP_IMG = new File(RecoveryTools.PathToRecoveries, DEVICE_NAME + "-twrp" + EXT);
        }
    }

    public boolean downloadUtils() {

        if (DEVICE_NAME.equals("montblanc")
                || DEVICE_NAME.equals("c6603")
                || DEVICE_NAME.equals("c6602")) {
            charger = new File(RecoveryTools.PathToUtils, "charger");
            chargermon = new File(RecoveryTools.PathToUtils, "chargermon");
            ric = new File(RecoveryTools.PathToUtils, "ric");

            if ((DEVICE_NAME.equals("montblanc") && !charger.exists() || !chargermon.exists())
                    || DEVICE_NAME.equals("c6602") || DEVICE_NAME.equals("c6603") && !charger.exists() || !chargermon.exists() || !ric.exists()) {
                AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(mContext);
                mAlertDialog
                        .setTitle(R.string.warning)
                        .setMessage(R.string.download_utils);
                final DialogInterface.OnClickListener onClick = new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final DialogInterface.OnClickListener loop = this;
                        final Runnable checkFile = new Runnable() {
                            @Override
                            public void run() {
                                boolean corrupt = false;
                                if (isDowloadCorrupt(chargermon)) {
                                    chargermon.delete();
                                    corrupt = true;
                                }
                                if (isDowloadCorrupt(charger)) {
                                    charger.delete();
                                    corrupt = true;
                                }
                                if (DEVICE_NAME.equals("c6603")
                                        || DEVICE_NAME.equals("c6602"))
                                    if (isDowloadCorrupt(ric)) {
                                        ric.delete();
                                        corrupt = true;
                                    }
                                if (corrupt) {
                                    final AlertDialog.Builder tryAgain = new AlertDialog.Builder(mContext);
                                    tryAgain
                                            .setMessage(R.string.corrupt_download)
                                            .setPositiveButton(R.string.try_again, loop)
                                            .setNegativeButton(R.string.later, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                }
                                            })
                                            .setTitle(R.string.warning)
                                            .show();
                                }
                            }
                        };
                        new Downloader(mContext, "http://dslnexus.nazuka.net/utils/" + DEVICE_NAME, chargermon.getName() + ".img", chargermon, new Runnable() {
                            @Override
                            public void run() {
                                new Downloader(mContext, "http://dslnexus.nazuka.net/utils/" + DEVICE_NAME, charger.getName() + ".img", charger, new Runnable() {
                                    @Override
                                    public void run() {
                                        if (DEVICE_NAME.equals("c6603")
                                                || DEVICE_NAME.equals("c6602"))
                                            new Downloader(mContext, "http://dslnexus.nazuka.net/utils/" + DEVICE_NAME, ric.getName() + ".img", ric, checkFile).execute();
                                        else
                                            checkFile.run();
                                    }
                                }).execute();
                            }
                        }).execute();
                    }
                };
                mAlertDialog.setPositiveButton(R.string.positive, onClick);
                mAlertDialog.show();
                return true;
            }
        }
    return false;
    }

    public boolean isDowloadCorrupt(File downloadFile) {
        Common mCommon = new Common();
        File CorruptDownload = new File(mContext.getFilesDir(), "corruptDownload");
        mCommon.pushFileFromRAW(mContext, CorruptDownload, R.raw.corrupt_download);
        return downloadFile.length() == CorruptDownload.length();
    }
}