const { Grade, Student, Subject } = require('../../../models');

const GradeController = {
  // Get all grades dengan relasi
  async getAll(request, h) {
    try {
      const grades = await Grade.findAll({
        include: [
          { model: Student, as: 'student' },
          { model: Subject, as: 'subject' }
        ]
      });
      return h.response(grades).code(200);
    } catch (error) {
      return h.response({ message: error.message }).code(500);
    }
  },

  // Get grade by ID dengan relasi
  async getById(request, h) {
    try {
      const { id } = request.params;
      const grade = await Grade.findByPk(id, {
        include: [
          { model: Student, as: 'student' },
          { model: Subject, as: 'subject' }
        ]
      });
      
      if (!grade) {
        return h.response({ message: 'Grade not found' }).code(404);
      }
      return h.response(grade).code(200);
    } catch (error) {
      return h.response({ message: error.message }).code(500);
    }
  },

  // Create new grade dengan validasi relasi
  async create(request, h) {
    try {
      const { studentId, subjectId, score } = request.payload;
      
      // Validasi student dan subject exist
      const student = await Student.findByPk(studentId);
      const subject = await Subject.findByPk(subjectId);
      
      if (!student) {
        return h.response({ message: 'Student not found' }).code(404);
      }
      if (!subject) {
        return h.response({ message: 'Subject not found' }).code(404);
      }

      const newGrade = await Grade.create({ studentId, subjectId, score });
      return h.response(newGrade).code(201);
    } catch (error) {
      return h.response({ message: error.message }).code(500);
    }
  },

  // Update grade dengan validasi
  async update(request, h) {
    try {
      const { id } = request.params;
      const { studentId, subjectId, score } = request.payload;
  
      const grade = await Grade.findByPk(id);
      if (!grade) {
        return h.response({ message: 'Grade not found' }).code(404);
      }
  
      // Validasi student jika ada
      if (studentId !== undefined) {
        const student = await Student.findByPk(studentId);
        if (!student) {
          return h.response({ message: 'Student not found' }).code(404);
        }
        grade.studentId = studentId;
      }
  
      // Validasi subject jika ada
      if (subjectId !== undefined) {
        const subject = await Subject.findByPk(subjectId);
        if (!subject) {
          return h.response({ message: 'Subject not found' }).code(404);
        }
        grade.subjectId = subjectId;
      }
  
      // Update score jika ada
      if (score !== undefined) {
        grade.score = score;
      }
  
      await grade.save();
  
      // Ambil data lengkap dengan relasi
      const updatedGrade = await Grade.findByPk(id, {
        include: [
          { model: Student, as: 'student' },
          { model: Subject, as: 'subject' }
        ]
      });
  
      return h.response(updatedGrade).code(200);
    } catch (error) {
      return h.response({ message: error.message }).code(500);
    }
  },

  // Delete grade
  async delete(request, h) {
    try {
      const { id } = request.params;
      const grade = await Grade.findByPk(id);
      
      if (!grade) {
        return h.response({ message: 'Grade not found' }).code(404);
      }
      
      await grade.destroy();
      return h.response({ message: 'Grade deleted successfully' }).code(200);
    } catch (error) {
      return h.response({ message: error.message }).code(500);
    }
  }
};

module.exports = GradeController;