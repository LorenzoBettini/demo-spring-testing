package com.examples.spring.demo.model;

import java.util.Objects;

public class Employee {

	private Long id;
	private String name;
	private long salary;

	public Employee() {

	}

	public Employee(Long id, String name, long salary) {
		this.id = id;
		this.name = name;
		this.salary = salary;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public long getSalary() {
		return salary;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSalary(long salary) {
		this.salary = salary;
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", name=" + name + ", salary=" + salary + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, salary);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		return Objects.equals(id, other.id) && Objects.equals(name, other.name) && salary == other.salary;
	}

}