const { Parser } = require('json2csv');
const  models  = require('../../../models');

const convertToCSVController = {
    async studentToCsv(request, h) {
        try {
            const students = await models.Student.findAll({ raw: true });
            const parser = new Parser();
            const csv = parser.parse(students);

        return h.response(csv)
            .type('text/csv')
            .header('Content-Disposition', 'attachment; filename=students.csv');
        } catch (error) {
          return h.response({ message: error.message }).code(500);
        }
      },
    async subjectToCsv(request, h) {
        try {
            const subjects = await models.Subject.findAll({ raw: true });
            const parser = new Parser();
            const csv = parser.parse(subjects);

        return h.response(csv)
            .type('text/csv')
            .header('Content-Disposition', 'attachment; filename=subject.csv');
        } catch (error) {
          return h.response({ message: error.message }).code(500);
        }
      },
    async gradeToCsv(request, h) {
        try {
            const grades = await models.Grade.findAll({ raw: true });
            const parser = new Parser();
            const csv = parser.parse(grades);

        return h.response(csv)
            .type('text/csv')
            .header('Content-Disposition', 'attachment; filename=grade.csv');
        } catch (error) {
          return h.response({ message: error.message }).code(500);
        }
      },
}

module.exports = convertToCSVController;