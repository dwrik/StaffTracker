package org.dwrik.department.service;

import org.dwrik.department.entity.Department;
import org.dwrik.department.exception.DepartmentAlreadyExistsException;
import org.dwrik.department.repository.DepartmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTests {

	@Mock
	private DepartmentRepository departmentRepository;

	@InjectMocks
	private DepartmentService departmentService;

	@Test
	public void testCreateDepartment() {
		var expected = new Department(1L, "Engineering");
		var department = new Department(1L, "Engineering");

		Mockito.when(departmentRepository.existsByName(anyString())).thenReturn(Boolean.FALSE);
		Mockito.when(departmentRepository.save(department)).thenReturn(expected);

		var actual = departmentService.createDepartment(department);
		assertEquals(expected, actual);
	}

	@Test
	public void testCreateDepartmentWithInvalidName() {
		var department = new Department(1L, "Engineering");

		Mockito.when(departmentRepository.existsByName(anyString())).thenReturn(Boolean.TRUE);
		var exception = assertThrows(DepartmentAlreadyExistsException.class,
				() -> departmentService.createDepartment(department));

		assertEquals(
				"department with name '" + department.getName() + "' already exists",
				exception.getMessage());
	}
}
