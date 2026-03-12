package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ProfileController {

    @GetMapping("/profile")
    public String showProfile(Model model) {
        model.addAttribute("memberName", "Le Viet Hai Dang");
        model.addAttribute("memberRole", "Frontend UI - Profile page");
        model.addAttribute("memberCode", "TGDD-PROFILE-01");
        model.addAttribute("memberEmail", "haidang@student.local");
        model.addAttribute("memberPhone", "0900 123 456");
        model.addAttribute("memberLocation", "Ho Chi Minh City");
        model.addAttribute("memberAbout",
                "Trang profile nay tap trung vao thong tin ca nhan, tien do cong viec va phan hien thi san sang cho review.");
        model.addAttribute("skills", List.of("Spring Boot", "Thymeleaf", "Bootstrap 5", "Responsive UI"));
        model.addAttribute("stats", List.of(
                new ProfileStat("Task da hoan thanh", "08", "UI screens ready"),
                new ProfileStat("Pull request", "02", "1 cho review"),
                new ProfileStat("Commit trong tuan", "14", "phan bo deu"),
                new ProfileStat("Muc do san sang", "95%", "co the demo")
        ));
        model.addAttribute("timeline", List.of(
                new TimelineItem("Khoi tao giao dien", "Day 1", "Dung khung profile va bo cuc tong quan.", "Done"),
                new TimelineItem("Hoan thien noi dung", "Day 2", "Bo sung the thong tin, ky nang va muc tieu.", "Done"),
                new TimelineItem("Kiem tra responsive", "Day 3", "Canh chinh tren tablet va mobile.", "In review")
        ));
        model.addAttribute("highlights", List.of(
                "Thong tin duoc chia thanh tung khoi de chup anh ro rang.",
                "Mau sac dong bo theme trang chu hien tai.",
                "Cac nut action san cho luong review cua nhom."
        ));
        return "profile";
    }

    public record ProfileStat(String label, String value, String hint) {
    }

    public record TimelineItem(String title, String phase, String detail, String status) {
    }
}
