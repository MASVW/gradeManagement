'use strict';
const { Model } = require('sequelize');

module.exports = (sequelize, DataTypes) => {
  class Grade extends Model {
    static associate(models) {
      Grade.belongsTo(models.Student, {
        foreignKey: 'studentId', // kolom foreign key di tabel Grade
        as: 'student' // alias untuk query
      });
    
      // Relasi ke Subject
      Grade.belongsTo(models.Subject, {
        foreignKey: 'subjectId',
        as: 'subject'
      });
    }
  }

  Grade.init({
    id: {
      type: DataTypes.INTEGER,
      primaryKey: true,
      autoIncrement: true,
    },
    studentId: {
      type: DataTypes.INTEGER,
      allowNull: false,
    },
    subjectId: {
      type: DataTypes.INTEGER,
      allowNull: false,
    },
    score: {
      type: DataTypes.FLOAT,
    },
  }, {
    sequelize,
    modelName: 'Grade',
  });

  return Grade;
};
