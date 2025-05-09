const { Student } = require('../../../models');
const{ verifyToken } = require('../helpers/auth');

const StudentController = {
  async getAll(request, h) {
    try {
      const authHeader = request.headers.authorization;
      if (!authHeader) {
        return h.response({ error: 'Header Authorization diperlukan' }).code(400);
      }

      const token = authHeader.replace(/^Bearer\s+/i, '').trim();
      if (!token) {
        return h.response({ error: 'Format token tidak valid' }).code(400);
      }      

      const verified = await verifyToken(token);
      if (!verified) {
        return h.response({ 
          error: 'Auth failed',
          detail: 'Invalid or expired token' 
        }).code(401);
      }
  
      const students = await Student.findAll();
      return h.response(students).code(200);
    } catch (error) {
      return h.response({ message: error.message }).code(500);
    }
  },
  

  async getById(request, h) {
    try {
      const authHeader = request.headers.authorization;
      if (!authHeader) {
        return h.response({ error: 'Header Authorization diperlukan' }).code(400);
      }

      const token = authHeader.replace(/^Bearer\s+/i, '').trim();
      if (!token) {
        return h.response({ error: 'Format token tidak valid' }).code(400);
      }      

      const verified = await verifyToken(token);
      if (!verified) {
        return h.response({ 
          error: 'Auth failed',
          detail: 'Invalid or expired token' 
        }).code(401);
      }

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

  async create(request, h) {
    try {
      const authHeader = request.headers.authorization;
      if (!authHeader) {
        return h.response({ error: 'Header Authorization diperlukan' }).code(400);
      }

      const token = authHeader.replace(/^Bearer\s+/i, '').trim();
      if (!token) {
        return h.response({ error: 'Format token tidak valid' }).code(400);
      }      

      const verified = await verifyToken(token);
      if (!verified) {
        return h.response({ 
          error: 'Auth failed',
          detail: 'Invalid or expired token' 
        }).code(401);
      }

      const { name, age, class: className } = request.payload;
      const newStudent = await Student.create({ name, age, class: className });
      return h.response(newStudent).code(201);
    } catch (error) {
      return h.response({ message: error.message }).code(500);
    }
  },

  async update(request, h) {
    try {
      const authHeader = request.headers.authorization;
      if (!authHeader) {
        return h.response({ error: 'Header Authorization diperlukan' }).code(400);
      }

      const token = authHeader.replace(/^Bearer\s+/i, '').trim();
      if (!token) {
        return h.response({ error: 'Format token tidak valid' }).code(400);
      }      

      const verified = await verifyToken(token);
      if (!verified) {
        return h.response({ 
          error: 'Auth failed',
          detail: 'Invalid or expired token' 
        }).code(401);
      }

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

  async delete(request, h) {
    try {
      const authHeader = request.headers.authorization;
      if (!authHeader) {
        return h.response({ error: 'Header Authorization diperlukan' }).code(400);
      }

      const token = authHeader.replace(/^Bearer\s+/i, '').trim();
      if (!token) {
        return h.response({ error: 'Format token tidak valid' }).code(400);
      }      

      const verified = await verifyToken(token);
      if (!verified) {
        return h.response({ 
          error: 'Auth failed',
          detail: 'Invalid or expired token' 
        }).code(401);
      }

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
