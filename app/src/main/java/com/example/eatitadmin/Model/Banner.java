package com.example.eatitadmin.Model;

public class Banner {

    String BannerName,BannerImage,BannerId;

    public Banner() {
    }

    public Banner(String bannerName, String bannerImage, String bannerId) {
        BannerName = bannerName;
        BannerImage = bannerImage;
        BannerId = bannerId;
    }

    public String getBannerName() {
        return BannerName;
    }

    public void setBannerName(String bannerName) {
        BannerName = bannerName;
    }

    public String getBannerImage() {
        return BannerImage;
    }

    public void setBannerImage(String bannerImage) {
        BannerImage = bannerImage;
    }

    public String getBannerId() {
        return BannerId;
    }

    public void setBannerId(String bannerId) {
        BannerId = bannerId;
    }
}
