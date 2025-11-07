package fpt.com.capstone.service;

import fpt.com.capstone.dto.request.CreateCouncilRequest;
import fpt.com.capstone.dto.request.UpdateCouncilRequest;
import fpt.com.capstone.dto.response.CouncilResponse;
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

import java.util.ArrayList; // SỬA ĐỔI
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

        // SỬA ĐỔI: Phải lưu Council TRƯỚC để lấy ID
        Council savedCouncil = councilRepository.save(council);

        // SỬA ĐỔI: Dùng List để chuẩn bị saveAll
        List<CouncilMember> councilMembersToSave = new ArrayList<>();

        for (CreateCouncilRequest.CouncilMemberRequest memberDto : request.getMembers()) {
            Lecturer lecturer = lecturerRepository.findById(memberDto.getLecturerId()).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy giảng viên với ID: " + memberDto.getLecturerId()));
            CouncilMember.CouncilRole role = CouncilMember.CouncilRole.valueOf(memberDto.getRole().toUpperCase());

            CouncilMember councilMember = new CouncilMember();
            councilMember.setLecturer(lecturer);
            councilMember.setRole(role);
            // SỬA ĐỔI: Gán council đã được lưu (có ID) vào
            councilMember.setCouncil(savedCouncil);

            councilMembersToSave.add(councilMember);
        }

        // SỬA ĐỔI: Xóa dòng council.setCouncilMembers(...)
        // council.setCouncilMembers(councilMembers);

        // SỬA ĐỔI: Lưu các CouncilMember một cách thủ công
        councilMemberRepository.saveAll(councilMembersToSave);

        return savedCouncil; // Trả về council đã lưu
    }


    private void validateCouncilRules(List<CreateCouncilRequest.CouncilMemberRequest> members) {
        // (Hàm này giữ nguyên, không thay đổi)
        long distinctLecturers = members.stream().map(CreateCouncilRequest.CouncilMemberRequest::getLecturerId).distinct().count();
        if (distinctLecturers < members.size()) {
            throw new IllegalArgumentException("Một giảng viên không thể có 2 vai trò trong hội đồng.");
        }
        // ... (các logic đếm role khác)
    }

    public List<CouncilResponse> getAllCouncils() {

        // --- Tối ưu N+1 Query ---
        // 1. Lấy tất cả councils (Query 1)
        List<Council> councils = councilRepository.findAll();

        // 2. Lấy tất cả members (Query 2)
        List<CouncilMember> allMembers = councilMemberRepository.findAll();

        // 3. Nhóm members theo councilId để tra cứu O(1)
        Map<Integer, List<CouncilMember>> membersByCouncilId = allMembers.stream()
                .collect(Collectors.groupingBy(member -> member.getCouncil().getId()));
        // --- Hết phần tối ưu ---


        // 4. Map Council sang CouncilResponse
        return councils.stream().map(council -> {

            // Lấy danh sách member đã được nhóm
            List<CouncilMember> members = membersByCouncilId.getOrDefault(council.getId(), List.of());

            // 5. Map CouncilMember sang DTO con
            List<CouncilResponse.CouncilMemberResponse> memberResponses = members.stream()
                    .map(member -> CouncilResponse.CouncilMemberResponse.builder()
                            .memberId(member.getId())
                            .role(member.getRole().name())
                            .lecturerId(member.getLecturer().getId())
                            .lecturerName(member.getLecturer().getFullName())
                            .lecturerEmail(member.getLecturer().getEmail())
                            .build())
                    .collect(Collectors.toList());

            // 6. Xây dựng DTO cha
            return CouncilResponse.builder()
                    .id(council.getId())
                    .name(council.getName())
                    .description(council.getDescription())
                    .status(council.getStatus())
                    .semester(council.getSemester())
                    .members(memberResponses)
                    .build();

        }).collect(Collectors.toList());
    }

    public Council getCouncilById(int id) {
        return councilRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hội đồng với ID: " + id));
    }

    @Transactional
    public Council updateCouncil(int councilId, UpdateCouncilRequest request) {

        validateCouncilRules(request.getMembers());

        Council council = councilRepository.findById(councilId).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hội đồng với ID: " + councilId));

        // 1. Cập nhật thông tin cơ bản của Council
        council.setName(request.getName());
        council.setDescription(request.getDescription());

        if (council.getSemester() == null || council.getSemester().getId() != request.getSemesterId()) {
            Semester semester = semesterRepository.findById(request.getSemesterId()).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy học kỳ với ID: " + request.getSemesterId()));
            council.setSemester(semester);
        }
        // Lưu thay đổi của Council
        councilRepository.save(council);


        // 2. Đồng bộ (Sync) CouncilMembers
        Map<Integer, CreateCouncilRequest.CouncilMemberRequest> newMembersMap = request.getMembers().stream().collect(Collectors.toMap(CreateCouncilRequest.CouncilMemberRequest::getLecturerId, member -> member));

        // SỬA ĐỔI: Lấy thành viên cũ từ Repository, KHÔNG lấy từ council
        Set<CouncilMember> currentMembers = councilMemberRepository.findByCouncilId(councilId);

        Set<CouncilMember> membersToRemove = new HashSet<>();
        Set<CouncilMember> membersToUpdate = new HashSet<>();

        // (Logic diff/so sánh bên trong vòng for giữ nguyên)
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

        // (Logic tạo member mới giữ nguyên)
        Set<CouncilMember> membersToAdd = new HashSet<>();
        for (CreateCouncilRequest.CouncilMemberRequest newMemberDto : newMembersMap.values()) {
            Lecturer lecturer = lecturerRepository.findById(newMemberDto.getLecturerId()).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy giảng viên với ID: " + newMemberDto.getLecturerId()));

            CouncilMember newMember = new CouncilMember();
            newMember.setCouncil(council); // Gán council hiện tại
            newMember.setLecturer(lecturer);
            newMember.setRole(parseCouncilRole(newMemberDto.getRole()));

            membersToAdd.add(newMember);
        }

        // 3. Thực thi thay đổi thủ công
        if (!membersToRemove.isEmpty()) {
            // SỬA ĐỔI: Xóa dòng council.getCouncilMembers().removeAll(...)
            councilMemberRepository.deleteAll(membersToRemove); // Giữ dòng này
        }
        if (!membersToAdd.isEmpty()) {
            // SỬA ĐỔI: Xóa dòng council.getCouncilMembers().addAll(...)
            councilMemberRepository.saveAll(membersToAdd); // Thêm dòng này
        }
        if (!membersToUpdate.isEmpty()) {
            councilMemberRepository.saveAll(membersToUpdate); // Giữ dòng này
        }

        return council; // Trả về council đã được cập nhật
    }


    private CouncilMember.CouncilRole parseCouncilRole(String role) {
        // (Hàm này giữ nguyên)
        try {
            return CouncilMember.CouncilRole.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Vai trò không hợp lệ: " + role);
        }
    }
}