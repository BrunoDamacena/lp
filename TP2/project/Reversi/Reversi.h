#ifndef REVERSI_H
#define REVERSI_H

#include <QMainWindow>

class Cell;

namespace Ui {
    class Reversi;
}

class Reversi : public QMainWindow {
    Q_OBJECT

public:
    explicit Reversi(QWidget *parent = 0);
    ~Reversi();

private:
    Ui::Reversi *ui;
    Cell* m_cells[8][8];
    int m_count[2];
    int player = 1;

private slots:
    void play();
    void reset();
    void showAbout();
    void updateStatus(int oldPlayer, int newPlayer);
    bool verticalPos(int coluna,int linha);
    bool verticalNeg(int coluna,int linha);
    bool horizontalPos(int coluna,int linha);
    bool horizontalNeg(int coluna,int linha);
    bool rightDiagPos(int coluna,int linha);
    bool rightDiagNeg(int coluna,int linha);
    bool leftDiagPos(int coluna,int linha);
    bool leftDiagNeg(int coluna,int linha);
    bool verificaLados(Cell* cell);
    bool verticalp(int coluna,int linha);
    bool verticaln(int coluna,int linha);
    bool horizontalp(int coluna,int linha);
    bool horizontaln(int coluna,int linha);
    bool diagdirp(int coluna,int linha);
    bool diagdirn(int coluna,int linha);
    bool diagesqp(int coluna,int linha);
    bool diagesqn(int coluna,int linha);
    bool fimdejogo();
    bool verificafim(Cell* cell);
};

#endif // REVERSI_H
