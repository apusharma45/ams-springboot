package com.academic.ams.security;

import com.academic.ams.repository.StudentRepository;
import com.academic.ams.repository.TeacherRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebUserDetailsService implements UserDetailsService {

    private final StudentRepository studentRepo;
    private final TeacherRepository teacherRepo;

    public WebUserDetailsService(StudentRepository studentRepo, TeacherRepository teacherRepo) {
        this.studentRepo = studentRepo;
        this.teacherRepo = teacherRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        var tOpt = teacherRepo.findByTeacherId(username);
        if (tOpt.isPresent()) {
            var t = tOpt.get();
            return new User(
                    t.getTeacherId(),
                    t.getPasswordHash(),
                    List.of(new SimpleGrantedAuthority("ROLE_TEACHER"))
            );
        }

        var sOpt = studentRepo.findByStudentId(username);
        if (sOpt.isPresent()) {
            var s = sOpt.get();
            return new User(
                    s.getStudentId(),
                    s.getPasswordHash(),
                    List.of(new SimpleGrantedAuthority("ROLE_STUDENT"))
            );
        }

        throw new UsernameNotFoundException("User not found");
    }
}
