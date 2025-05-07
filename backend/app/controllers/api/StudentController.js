const { Student } = require('../../../models');


const StudentController = {
  // Get all students
  async getAll(request, h) {
    try {
      const students = await Student.findAll();
      return h.response(students).code(200);
    } catch (error) {
      return h.response({ message: error.message }).code(500);
    }
  },

  // Get student by ID
  async getById(request, h) {
    try {
      const { id } = request.params;
      const student = await Student.findByPk(id);
      if (!student) {
        return h.response({ message: 'Student not found' }).code(404);
      }
      return h.response(student).code(200);
    } catch (error) {
      return h.response({ message: error.message }).code(500);
    }
  },

  // Create new student
  async create(request, h) {
    try {
      const { name, age, class: className } = request.payload;
      const newStudent = await Student.create({ name, age, class: className });
      return h.response(newStudent).code(201);
    } catch (error) {
      return h.response({ message: error.message }).code(500);
    }
  },

  // Update student by ID
  async update(request, h) {
    try {
      const { id } = request.params;
      const { name, age, class: className } = request.payload;
      const student = await Student.findByPk(id);
      if (!student) {
        return h.response({ message: 'Student not found' }).code(404);
      }
      await student.update({ name, age, class: className });
      return h.response(student).code(200);
    } catch (error) {
      return h.response({ message: error.message }).code(500);
    }
  },

  // Delete student by ID
  async delete(request, h) {
    try {
      const { id } = request.params;
      const student = await Student.findByPk(id);
      if (!student) {
        return h.response({ message: 'Student not found' }).code(404);
      }
      await student.destroy();
      return h.response({ message: 'Student deleted successfully' }).code(200);
    } catch (error) {
      return h.response({ message: error.message }).code(500);
    }
  }
};

module.exports = StudentController;
