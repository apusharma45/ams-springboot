package com.academic.ams.config;

import com.academic.ams.entity.Dept;
import com.academic.ams.entity.Student;
import com.academic.ams.entity.Teacher;
import com.academic.ams.repository.DeptRepository;
import com.academic.ams.repository.StudentRepository;
import com.academic.ams.repository.TeacherRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seed(DeptRepository deptRepo,
                           StudentRepository studentRepo,
                           TeacherRepository teacherRepo,
                           PasswordEncoder encoder) {
        return args -> {

            Dept cse = deptRepo.findByCode("CSE").orElseGet(() -> {
                Dept d = new Dept();
                d.setCode("CSE");
                d.setName("Computer Science and Engineering");
                return deptRepo.save(d);
            });

            teacherRepo.findByTeacherId("T-1001").orElseGet(() -> {
                Teacher t = new Teacher();
                t.setTeacherId("T-1001");
                t.setName("Dr. Rahman");
                t.setEmail("rahman@uni.edu");
                t.setDept(cse);
                t.setPasswordHash(encoder.encode("teacher123"));
                return teacherRepo.save(t);
            });

            studentRepo.findByStudentId("2020-1-60-001").orElseGet(() -> {
                Student s = new Student();
                s.setStudentId("2020-1-60-001");
                s.setName("Apu");
                s.setEmail("apu@student.edu");
                s.setPhone("01700000000");
                s.setDept(cse);
                s.setPasswordHash(encoder.encode("student123"));
                return studentRepo.save(s);
            });
        };
    }
}
