'use strict';
/** @type {import('sequelize-cli').Migration} */
module.exports = {
  async up(queryInterface, Sequelize) {
    await queryInterface.createTable('Grades', {
      id: {
        allowNull: false,
        autoIncrement: true,
        primaryKey: true,
        type: Sequelize.INTEGER
      },
      studentId: {
        type: Sequelize.INTEGER,
        references: {
          model: 'Students', // Make sure this matches your actual table name case
          key: 'id'
        },
        onDelete: 'CASCADE', // This is the key part - specify CASCADE directly here
        onUpdate: 'CASCADE'
      },
      subjectId: {
        type: Sequelize.INTEGER,
        references: {
          model: 'Subjects',
          key: 'id'
        }
      },
      score: {
        type: Sequelize.FLOAT
      },
      createdAt: {
        allowNull: false,
        type: Sequelize.DATE
      },
      updatedAt: {
        allowNull: false,
        type: Sequelize.DATE
      }
    });
    // No need to add/remove constraints again as they're already defined above
  },

  async down(queryInterface, Sequelize) {
    await queryInterface.dropTable('Grades');
  }
};