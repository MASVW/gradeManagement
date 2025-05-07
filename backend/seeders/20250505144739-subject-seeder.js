'use strict';

/** @type {import('sequelize-cli').Seeder} */
module.exports = {
  async up(queryInterface, Sequelize) {
    await queryInterface.bulkInsert('Subjects', [
      {
        name: 'Mathematics',
        description: 'This subject covers topics related to numbers, shapes, and equations.',
        createdAt: new Date(),
        updatedAt: new Date()
      },
      {
        name: 'Science',
        description: 'This subject covers topics related to physics, chemistry, and biology.',
        createdAt: new Date(),
        updatedAt: new Date()
      },
      {
        name: 'History',
        description: 'This subject covers the study of past events and civilizations.',
        createdAt: new Date(),
        updatedAt: new Date()
      },
      {
        name: 'English Literature',
        description: 'This subject focuses on studying literature, poetry, and language.',
        createdAt: new Date(),
        updatedAt: new Date()
      }
    ], {});
  },

  async down(queryInterface, Sequelize) {
    await queryInterface.bulkDelete('Subjects', null, {});
  }
};
