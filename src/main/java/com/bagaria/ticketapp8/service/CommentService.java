package com.bagaria.ticketapp8.service;

import com.bagaria.ticketapp8.dto.CommentRequest;
import com.bagaria.ticketapp8.entity.Comment;
import com.bagaria.ticketapp8.entity.Ticket;
import com.bagaria.ticketapp8.entity.User;
import com.bagaria.ticketapp8.exception.TicketNotFoundException;
import com.bagaria.ticketapp8.exception.UnauthorizedActionException;
import com.bagaria.ticketapp8.repository.CommentRepository;
import com.bagaria.ticketapp8.repository.TicketRepository;
import com.bagaria.ticketapp8.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public String addComment(
            Integer ticketId,
            CommentRequest request,
            Authentication authentication) {

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(ticketId));

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UnauthorizedActionException(authentication.getName()));

        Comment comment = new Comment();

        comment.setTicket(ticket);
        //comment.setTicketId(ticket);
        comment.setMessage(request.comment());
        comment.setCreatedBy(user);
        comment.setCreatedDate(LocalDateTime.now());
        comment.setUpdatedDate(LocalDateTime.now());

        commentRepository.save(comment);

        return "Comment added successfully";
    }

    public List<String> getComments(Integer ticketId) {
        return commentRepository.findMessagesByTicketId(ticketId);
    }
}
