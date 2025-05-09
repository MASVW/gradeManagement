'use strict';

/** @type {import('sequelize-cli').Seeder} */
module.exports = {
  async up(queryInterface, Sequelize) {
    await queryInterface.bulkInsert('Grades', [
      {
        studentId: 1,
        subjectId: 1,  
        score: 85,
        createdAt: new Date(),
        updatedAt: new Date()
      },
      {
        studentId: 2,
        subjectId: 2,
        score: 90,
        createdAt: new Date(),
        updatedAt: new Date()
      },
      {
        studentId: 3,
        subjectId: 3,
        score: 78,
        createdAt: new Date(),
        updatedAt: new Date()
      },
      {
        studentId: 4,
        subjectId: 4,
        score: 88,
        createdAt: new Date(),
        updatedAt: new Date()
      }
    ], {});
  },

  async down(queryInterface, Sequelize) {
    await queryInterface.bulkDelete('Grades', null, {});
  }
};
