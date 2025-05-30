/*package database.cms.service;

import database.cms.DTO.request.*;
import database.cms.DTO.response.*;
import database.cms.entity.*;
import database.cms.exception.BusinessErrorException;
import database.cms.exception.ResourceNotFoundException;
import database.cms.repository.AppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    private User testUser;
    private Vehicle testVehicle;
    private Appointment testAppointment;
    private LocalDateTime testAppointmentTime;
    private Technician testTechnician;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        testUser = new User(1L, "testUser", Role.USER, "123");
        testVehicle = new Vehicle(1L, testUser, "Toyota", "Camry", "123");
        testTechnician = new Technician(1L, "tech1", "456");
        testAppointmentTime = LocalDateTime.now().plusDays(1);
        testAppointment = new Appointment(1L, testUser, testVehicle, testTechnician, Appointment.Status.UNACCEPTED);

        // 公共stub配置（使用lenient避免严格模式冲突）
        lenient().when(appointmentRepository.findById(1L)).thenReturn(Optional.of(testAppointment));
        lenient().when(appointmentRepository.findAll()).thenReturn(List.of(testAppointment));
    }

    @Test
    void createAppointment_ShouldReturnAppointmentResponse() {
        // Arrange
        AppointmentRequest request = new AppointmentRequest(testUser, testAppointmentTime, testVehicle);

        // Act
        AppointmentResponse response = appointmentService.createAppointment(request);

        // Assert
        assertNotNull(response);
        assertNotNull(response.appointment());
        assertEquals(testUser, response.appointment().getUser());
        assertEquals(testAppointmentTime, response.appointment().getAppointmentTime());
        assertEquals(testVehicle, response.appointment().getVehicle());
        assertEquals(Appointment.Status.UNACCEPTED, response.appointment().getStatus());
        assertNull(response.appointment().getTechnician()); // 新预约应该还没有分配技师
    }

    @Test
    void getAllAppointment_ShouldReturnAllAppointments() {
        // Act
        AllAppointmentResponse response = appointmentService.getAllAppointment();

        // Assert
        assertNotNull(response);
        assertEquals(1, response.appointments().size());
        assertEquals(testAppointment, response.appointments().getFirst());
        verify(appointmentRepository, times(1)).findAll();
    }

    @Test
    void getAppointmentDetail_WithValidId_ShouldReturnAppointment() {
        // Act
        AppointmentDetailResponse response = appointmentService.getAppointmentDetail(1L);

        // Assert
        assertNotNull(response);
        assertEquals(testAppointment, response.appointment());
        assertEquals(testTechnician, response.appointment().getTechnician());
        verify(appointmentRepository, times(1)).findById(1L);
    }

    @Test
    void getAppointmentDetail_WithInvalidId_ShouldThrowException() {
        // Arrange
        when(appointmentRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> appointmentService.getAppointmentDetail(999L));
        assertEquals("无效订单号", exception.getMessage());
        verify(appointmentRepository, times(1)).findById(999L);
    }

    @Test
    void reviseAppointment_WithValidRequest_ShouldUpdateAppointment() {
        // Arrange
        LocalDateTime newTime = testAppointmentTime.plusHours(2);
        Technician newTechnician = new Technician(2L, "tech2", "789");
        Appointment revisedAppointment = new Appointment(1L, testUser, testVehicle, newTechnician, Appointment.Status.ONGOING);
        AppointmentRevisionRequest request = new AppointmentRevisionRequest(1L, revisedAppointment);

        when(appointmentRepository.save(any(Appointment.class))).thenReturn(revisedAppointment);

        // Act
        AppointmentRevisionResponse response = appointmentService.reviseAppointment(request);

        // Assert
        assertTrue(response.success());
        verify(appointmentRepository, times(1)).save(revisedAppointment);
        assertEquals(newTime, revisedAppointment.getAppointmentTime());
        assertEquals(newTechnician, revisedAppointment.getTechnician());
        assertEquals(Appointment.Status.ONGOING, revisedAppointment.getStatus());
    }

    @Test
    void reviseAppointment_WithUnmatchedId_ShouldThrowException() {
        // Arrange
        Appointment revisedAppointment = new Appointment(2L, testUser, testVehicle, testTechnician,
                 Appointment.Status.UNACCEPTED);
        AppointmentRevisionRequest request = new AppointmentRevisionRequest(1L, revisedAppointment);

        // Act & Assert
        BusinessErrorException exception = assertThrows(BusinessErrorException.class,
                () -> appointmentService.reviseAppointment(request));
        assertEquals("订单不匹配", exception.getMessage());
        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void cancelAppointment_WithValidRequest_ShouldCancelAppointment() {
        // Arrange
        AppointmentCancelRequest request = new AppointmentCancelRequest(testAppointment);
        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        AppointmentCancelResponse response = appointmentService.cancelAppointment(request);

        // Assert
        assertTrue(response.success());
        assertEquals(Appointment.Status.CANCELLED, testAppointment.getStatus());
        assertNotNull(testAppointment.getUpdatedAt());
        verify(appointmentRepository, times(1)).save(testAppointment);
    }

    @Test
    void cancelAppointment_WithAlreadyCancelled_ShouldThrowException() {
        // Arrange
        testAppointment.setStatus(Appointment.Status.CANCELLED);
        AppointmentCancelRequest request = new AppointmentCancelRequest(testAppointment);

        // Act & Assert
        BusinessErrorException exception = assertThrows(BusinessErrorException.class,
                () -> appointmentService.cancelAppointment(request));
        assertEquals("预约已取消，无需重复操作", exception.getMessage());
        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void confirmAppointment_WithValidRequest_ShouldConfirmAppointment() {
        // Arrange
        AppointmentConfirmationRequest request = new AppointmentConfirmationRequest(testAppointment);
        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        AppointmentConfirmationResponse response = appointmentService.confirmAppointment(request);

        // Assert
        assertTrue(response.success());
        assertEquals(Appointment.Status.ONGOING, testAppointment.getStatus());
        assertNotNull(testAppointment.getUpdatedAt());
        assertEquals(1L, response.appointmentId());
        verify(appointmentRepository, times(1)).save(testAppointment);
    }

    @Test
    void confirmAppointment_WithInvalidStatus_ShouldThrowException() {
        // Arrange
        testAppointment.setStatus(Appointment.Status.CANCELLED);
        AppointmentConfirmationRequest request = new AppointmentConfirmationRequest(testAppointment);

        // Act & Assert
        BusinessErrorException exception = assertThrows(BusinessErrorException.class,
                () -> appointmentService.confirmAppointment(request));
        assertEquals("当前状态无法确认预约", exception.getMessage());
        verify(appointmentRepository, never()).save(any());
    }
}*/