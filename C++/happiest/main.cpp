#include <iostream>
#include <vector>
#include <fstream>
#include <random>
#include <algorithm>
#include "happycash.h"
#include <sstream>

using namespace std;

int main() {

    srand(time(0));  // 使用当前时间作为种子
    HappyCash* test = new HappyCash();
    if (test == nullptr) {
        cout << "new HappyCash failed!" << endl;
        return 0;
    }


    char input;
    cout << "input cmd: " << endl;
    cout << "   1->print sort arrays" << endl;
    cout << "   2->check one array" << endl;
    cout << "   3->get random data" << endl;
    cout << "   q->quit" << endl;
    while (true) {
        cin >> input;
        if (input == '1') {
            test->PrintSortArray();
        }else if (input == '2'){
            cout << "input your check numbers: ";
            //接收终端输入的一串字符串
            string str;
            cin >> str;
            //将字符串转换为int数组
            int arr[ARRAY_SIZE];
            int i = 0;
            for (int i = 0; i < ARRAY_SIZE; i++) {
                arr[i] = stoi(str.substr(i * 2, 2));
            }
//            cout << "input array: ";
//            for (int i = 0; i < ARRAY_SIZE; i++) {
//                cout << arr[i] << " ";
//            }
            cout << endl;
            test->CheckOneArray(arr);
        }else if (input == '3') {
            test->ClearResult();
            test->QueryRandomResultFive(3, 1, 1);
            test->QueryRandomResultTwo(1, 0, 1);
            test->PrintResult();
        }else if (input == 'q') {
            delete test;
            break;
        }
    }
    return 0;
}