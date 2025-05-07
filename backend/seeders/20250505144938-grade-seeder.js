'use strict';

/** @type {import('sequelize-cli').Seeder} */
module.exports = {
  async up(queryInterface, Sequelize) {
    await queryInterface.bulkInsert('Grades', [
      {
        studentId: 1,  // ID siswa, pastikan ID ini sesuai dengan data yang ada di tabel Students
        subjectId: 1,  // ID mata pelajaran, pastikan ID ini sesuai dengan data yang ada di tabel Subjects
        score: 85.5,
        createdAt: new Date(),
        updatedAt: new Date()
      },
      {
        studentId: 2,
        subjectId: 2,
        score: 90.0,
        createdAt: new Date(),
        updatedAt: new Date()
      },
      {
        studentId: 3,
        subjectId: 3,
        score: 78.3,
        createdAt: new Date(),
        updatedAt: new Date()
      },
      {
        studentId: 4,
        subjectId: 4,
        score: 88.7,
        createdAt: new Date(),
        updatedAt: new Date()
      }
    ], {});
  },

  async down(queryInterface, Sequelize) {
    await queryInterface.bulkDelete('Grades', null, {});
  }
};
