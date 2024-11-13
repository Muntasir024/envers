package com.example.envers.listener;

import com.example.envers.model.Revision;
import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class EntityRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        Revision revision = (Revision) revisionEntity;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
        Object details = auth.getDetails();

        if (principal instanceof UserDetails)
            revision.setUsername(((UserDetails) principal).getUsername());

        if (details instanceof WebAuthenticationDetails)
            revision.setIp(((WebAuthenticationDetails) details).getRemoteAddress());
    }
}
