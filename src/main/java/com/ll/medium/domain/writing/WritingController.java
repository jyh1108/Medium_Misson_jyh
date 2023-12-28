package com.ll.medium.domain.writing;

import com.ll.medium.domain.comment.CommentForm;
import com.ll.medium.domain.member.MemberEntity;
import com.ll.medium.domain.member.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RequestMapping("/writing")
@RequiredArgsConstructor
@Controller
public class WritingController {


    private final MemberService memberService;
    private final WritingService writingService;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page) {
        Page<Writing> paging = this.writingService.getList(page);
        model.addAttribute("paging", paging);
        return "writing_list";
    }
    @GetMapping("/myList")
    public String myList(Model model, @RequestParam(value="page", defaultValue="0") int page, Principal principal) {
        // 로그인한 사용자의 이름을 가져옵니다.
        String username = principal.getName();
        // 로그인한 사용자의 글만 가져옵니다.
        Page<Writing> paging = this.writingService.getMyList(username, page);
        model.addAttribute("paging", paging);
        return "writing_mylist";
    }

    @GetMapping("/{username}")
    public String getWritingByAuthor(@PathVariable String username, Model model, @RequestParam(defaultValue = "0") int page) {
        Page<Writing> writings = writingService.findByAuthor(username, page);
        MemberEntity author = memberService.getUser(username);
        model.addAttribute("author",author);
        model.addAttribute("writings", writings);
        return "writing_idList";

    }

    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, CommentForm commentForm) {
        Writing writing = this.writingService.getWriting(id);

        // 유료 멤버십 확인
        boolean isPaidMember = writing.getAuthor().isPaid();
        model.addAttribute("writing", writing);
        model.addAttribute("isPaidMember", isPaidMember);
        return "writing_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String writingCreate(WritingForm writingForm, Model model, Principal principal) {
        String username = principal.getName(); // 현재 로그인한 사용자의 이름을 얻음
        MemberEntity loggedInUser = memberService.getUser(username); // 사용자 이름으로 사용자 객체를 가져옴
        boolean isPaid = loggedInUser.isPaid(); // paid 필드 값을 얻음
        model.addAttribute("loggedInUserPaid", isPaid); // 모델에 로그인한 사용자의 paid 값을 추가
        return "writing_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String writingCreate(@Valid WritingForm writingForm, BindingResult bindingResult, Principal principal, Model model) {
        if (bindingResult.hasErrors()) {
            String username = principal.getName();
            MemberEntity loggedInUser = memberService.getUser(username);
            boolean isPaid = loggedInUser.isPaid(); // paid 필드 값을 얻음
            model.addAttribute("loggedInUserPaid", isPaid); // 모델에 로그인한 사용자의 paid 값을 추가
            return "writing_form";
        }

        MemberEntity memberEntity = this.memberService.getUser(principal.getName());

        // 유료 글인지 확인
        boolean isPaid = writingForm.isPaid();

        // 유료 글인 경우에만 paid 필드를 true로 설정
        if (isPaid) {
            memberEntity.setPaid(true);
        }

        this.writingService.create(writingForm.getSubject(), writingForm.getContent(), memberEntity, writingForm.isPublished(), writingForm.isPaid());
        return "redirect:/writing/list";
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String writingModify(WritingForm writingForm, @PathVariable("id") Integer id, Principal principal,Model model) {
        Writing writing = this.writingService.getWriting(id);
        if(!writing.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        String username = principal.getName();
        MemberEntity loggedInUser = memberService.getUser(username);
        boolean isPaid = loggedInUser.isPaid(); // paid 필드 값을 얻음
        model.addAttribute("loggedInUserPaid", isPaid);

        writingForm.setSubject(writing.getSubject());
        writingForm.setContent(writing.getContent());
        return "writing_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String writingModify(@Valid WritingForm writingForm, BindingResult bindingResult,Model model,
                                 Principal principal, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            String username = principal.getName();
            MemberEntity loggedInUser = memberService.getUser(username);
            boolean isPaid = loggedInUser.isPaid(); // paid 필드 값을 얻음
            model.addAttribute("loggedInUserPaid", isPaid);
            return "writing_form";
        }
        Writing writing = this.writingService.getWriting(id);
        if (!writing.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.writingService.modify(writing, writingForm.getSubject(), writingForm.getContent(), writingForm.isPublished());
        return String.format("redirect:/writing/detail/%s", id);
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String writingDelete(Principal principal, @PathVariable("id") Integer id) {
        Writing writing = this.writingService.getWriting(id);
        if (!writing.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.writingService.delete(writing);
        return "redirect:/";
    }
}
