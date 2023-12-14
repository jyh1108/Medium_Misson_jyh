package com.ll.medium.domain.comment;

import com.ll.medium.domain.member.MemberEntity;
import com.ll.medium.domain.member.MemberService;
import com.ll.medium.domain.writing.Writing;
import com.ll.medium.domain.writing.WritingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RequestMapping("/comment")
@RequiredArgsConstructor
@Controller
public class CommentController {
    private final WritingService writingService;
    private final CommentService commentService;
    private final MemberService memberService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createComment(Model model, @PathVariable("id") Integer id,
                               @Valid CommentForm commentForm, BindingResult bindingResult , Principal principal) {
        Writing writing = this.writingService.getWriting(id);
        MemberEntity memberEntity = this.memberService.getUser(principal.getName() );
        if (bindingResult.hasErrors()) {
            model.addAttribute("writing", writing);
            return "writing_detail";
        }
        Comment comment = this.commentService.create(writing,
                commentForm.getContent(), memberEntity);
        return String.format("redirect:/writing/detail/%s#comment_%s",
                comment.getWriting().getId(), comment.getId());
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String commentModify(CommentForm commentForm, @PathVariable("id") Integer id, Principal principal) {
        Comment comment = this.commentService.getComment(id);
        if (!comment.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.commentService.modify(comment, commentForm.getContent());
        return String.format("redirect:/writing/detail/%s#comment_%s",
                comment.getWriting().getId(), comment.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String commentDelete(Principal principal, @PathVariable("id") Integer id) {
        Comment comment = this.commentService.getComment(id);
        if (!comment.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.commentService.delete(comment);
        return String.format("redirect:/writing/detail/%s", comment.getWriting().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String commentModify(@Valid CommentForm commentForm, BindingResult bindingResult,
                               @PathVariable("id") Integer id, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "comment_form";
        }
        Comment comment = this.commentService.getComment(id);
        if (!comment.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.commentService.modify(comment, commentForm.getContent());
        return String.format("redirect:/question/detail/%s", comment.getWriting().getId());
    }

//    @PreAuthorize("isAuthenticated()")
//    @GetMapping("/vote/{id}")
//    public String answerVote(Principal principal, @PathVariable("id") Integer id) {
//        Answer answer = this.answerService.getAnswer(id);
//        SiteUser siteUser = this.userService.getUser(principal.getName());
//        this.answerService.vote(answer, siteUser);
//        return String.format("redirect:/question/detail/%s#answer_%s",
//                answer.getQuestion().getId(), answer.getId());
//    }
}
