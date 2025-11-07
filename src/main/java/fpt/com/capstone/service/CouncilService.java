package fpt.com.capstone.service;

import fpt.com.capstone.dto.request.CreateCouncilRequest;
import fpt.com.capstone.dto.request.UpdateCouncilRequest;
import fpt.com.capstone.model.Council;
import fpt.com.capstone.model.CouncilMember;
import fpt.com.capstone.model.Lecturer;
import fpt.com.capstone.model.Semester;
import fpt.com.capstone.repository.CouncilMemberRepository;
import fpt.com.capstone.repository.CouncilRepository;
import fpt.com.capstone.repository.LecturerRepository;
import fpt.com.capstone.repository.SemesterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouncilService {

    private final CouncilRepository councilRepository;
    private final LecturerRepository lecturerRepository;
    private final SemesterRepository semesterRepository;
    private final CouncilMemberRepository councilMemberRepository;

    @Transactional
    public Council createCouncil(CreateCouncilRequest request) {

        validateCouncilRules(request.getMembers());

        Semester semester = semesterRepository.findById(request.getSemesterId()).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy học kỳ với ID: " + request.getSemesterId()));

        Council council = new Council();
        council.setName(request.getName());
        council.setDescription(request.getDescription());
        council.setSemester(semester);
        council.setStatus(1);

        Set<CouncilMember> councilMembers = new HashSet<>();
        for (CreateCouncilRequest.CouncilMemberRequest memberDto : request.getMembers()) {

            Lecturer lecturer = lecturerRepository.findById(memberDto.getLecturerId()).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy giảng viên với ID: " + memberDto.getLecturerId()));

            CouncilMember.CouncilRole role =  CouncilMember.CouncilRole.valueOf(memberDto.getRole().toUpperCase());

            CouncilMember councilMember = new CouncilMember();
            councilMember.setLecturer(lecturer);
            councilMember.setRole(role);
            councilMember.setCouncil(council);

            councilMembers.add(councilMember);
        }

        council.setCouncilMembers(councilMembers);

       return councilRepository.save(council);
    }


    private void validateCouncilRules(List<CreateCouncilRequest.CouncilMemberRequest> members) {

        long distinctLecturers = members.stream().map(CreateCouncilRequest.CouncilMemberRequest::getLecturerId).distinct().count();
        if (distinctLecturers < members.size()) {
            throw new IllegalArgumentException("Một giảng viên không thể có 2 vai trò trong hội đồng.");
        }

        long presidentCount = members.stream().filter(m -> "PRESIDENT".equalsIgnoreCase(m.getRole())).count();
        long secretaryCount = members.stream().filter(m -> "SECRETARY".equalsIgnoreCase(m.getRole())).count();
        long reviewerCount = members.stream().filter(m -> "REVIEWER".equalsIgnoreCase(m.getRole())).count();

        if (presidentCount != 1) {
            throw new IllegalArgumentException("Hội đồng phải có đúng 1 Chủ tịch (PRESIDENT).");
        }
        if (secretaryCount != 1) {
            throw new IllegalArgumentException("Hội đồng phải có đúng 1 Thư ký (SECRETARY).");
        }
        if (reviewerCount != 3) {
            throw new IllegalArgumentException("Hội đồng phải có đúng 3 Giám khảo (REVIEWER).");
        }
    }

    public List< Council> getAllCouncils() {
        return councilRepository.findAll();
    }

    public Council getCouncilById(int id) {
        return councilRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hội đồng với ID: " + id));
    }

    @Transactional
    public  Council updateCouncil(int councilId, UpdateCouncilRequest request) {

        validateCouncilRules(request.getMembers());

        Council council = councilRepository.findById(councilId).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hội đồng với ID: " + councilId));

        council.setName(request.getName());
        council.setDescription(request.getDescription());

        if (council.getSemester() == null || council.getSemester().getId() != request.getSemesterId()) {
            Semester semester = semesterRepository.findById(request.getSemesterId()).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy học kỳ với ID: " + request.getSemesterId()));
            council.setSemester(semester);
        }

        Map<Integer, CreateCouncilRequest.CouncilMemberRequest> newMembersMap = request.getMembers().stream().collect(Collectors.toMap(CreateCouncilRequest.CouncilMemberRequest::getLecturerId, member -> member));

        Set<CouncilMember> currentMembers = council.getCouncilMembers();

        Set<CouncilMember> membersToRemove = new HashSet<>();
        Set<CouncilMember> membersToUpdate = new HashSet<>();

        for (CouncilMember currentMember : currentMembers) {
            int lecturerId = currentMember.getLecturer().getId();

            if (!newMembersMap.containsKey(lecturerId)) {
                membersToRemove.add(currentMember);
            } else {
                CreateCouncilRequest.CouncilMemberRequest newMemberDto = newMembersMap.get(lecturerId);
                CouncilMember.CouncilRole newRole = parseCouncilRole(newMemberDto.getRole());

                if (currentMember.getRole() != newRole) {
                    currentMember.setRole(newRole);
                    membersToUpdate.add(currentMember);
                }
                newMembersMap.remove(lecturerId);
            }
        }

        Set<CouncilMember> membersToAdd = new HashSet<>();
        for (CreateCouncilRequest.CouncilMemberRequest newMemberDto : newMembersMap.values()) {
            Lecturer lecturer = lecturerRepository.findById(newMemberDto.getLecturerId()).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy giảng viên với ID: " + newMemberDto.getLecturerId()));

            CouncilMember newMember = new CouncilMember();
            newMember.setCouncil(council);
            newMember.setLecturer(lecturer);
            newMember.setRole(parseCouncilRole(newMemberDto.getRole()));

            membersToAdd.add(newMember);
        }

        if (!membersToRemove.isEmpty()) {
            council.getCouncilMembers().removeAll(membersToRemove);
            councilMemberRepository.deleteAll(membersToRemove);
        }
        if (!membersToAdd.isEmpty()) {
            council.getCouncilMembers().addAll(membersToAdd);
        }
        if (!membersToUpdate.isEmpty()) {
            councilMemberRepository.saveAll(membersToUpdate);
        }

       return  councilRepository.save(council);
    }


    private CouncilMember.CouncilRole parseCouncilRole(String role) {
        try {
            return CouncilMember.CouncilRole.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Vai trò không hợp lệ: " + role);
        }
    }
}
