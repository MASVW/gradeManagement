const { Subject } = require('../../../models');

const SubjectController = {
  // Get all subjects
  async getAll(request, h) {
    try {
      const subjects = await Subject.findAll();
      return h.response(subjects).code(200);
    } catch (error) {
      return h.response({ message: error.message }).code(500);
    }
  },

  // Get subject by ID
  async getById(request, h) {
    try {
      const { id } = request.params;
      const subject = await Subject.findByPk(id);
      if (!subject) {
        return h.response({ message: 'Subject not found' }).code(404);
      }
      return h.response(subject).code(200);
    } catch (error) {
      return h.response({ message: error.message }).code(500);
    }
  },

  // Create new subject
  async create(request, h) {
    try {
      const { name, description } = request.payload;
      const newSubject = await Subject.create({ name, description });
      return h.response(newSubject).code(201);
    } catch (error) {
      return h.response({ message: error.message }).code(500);
    }
  },

  // Update subject by ID
  async update(request, h) {
    try {
      const { id } = request.params;
      const { name, description } = request.payload;
      const subject = await Subject.findByPk(id);
      if (!subject) {
        return h.response({ message: 'Subject not found' }).code(404);
      }
      await subject.update({ name, description });
      return h.response(subject).code(200);
    } catch (error) {
      return h.response({ message: error.message }).code(500);
    }
  },

  // Delete subject by ID
  async delete(request, h) {
    try {
      const { id } = request.params;
      const subject = await Subject.findByPk(id);
      if (!subject) {
        return h.response({ message: 'Subject not found' }).code(404);
      }
      await subject.destroy();
      return h.response({ message: 'Subject deleted successfully' }).code(200);
    } catch (error) {
      return h.response({ message: error.message }).code(500);
    }
  }
};

module.exports = SubjectController;
