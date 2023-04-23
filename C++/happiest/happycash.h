//
// Created by yi.ji on 2023/4/23.
//

#ifndef HAPPIEST_HAPPYCASH_H
#define HAPPIEST_HAPPYCASH_H

#include <vector>
#include <set>

enum value {
    ARRAY_LIMIT = 100,
    ARRAY_SIZE = 7,
    FIVE_RANGE = 35,
    TWO_RANGE = 12,
};

class HappyCash {
public:
    HappyCash();
    ~HappyCash();
    void CalculateFrequency();
    void QueryRandomResultFive(int head_choose, int mid_choose, int tail_choose);
    void QueryRandomResultTwo(int head_choose, int mid_choose, int tail_choose);
    void ClearResult();
    void PrintResult();
    void CheckOneArray(int* array);

private:
    int* source = nullptr;
    int* freq_five = nullptr;
    int* freq_two = nullptr;
    std::vector<std::pair<int, int>> freq_five_sort;
    std::vector<std::pair<int, int>> freq_two_sort;
    int* five_selected_head = nullptr;  //head equel 1~12
    int* five_selected_mid = nullptr;   //mid equel 13~25
    int* five_selected_tail = nullptr;  //tail equel 26~35
    int* two_selected_head = nullptr;  //head equel 1~4
    int* two_selected_mid = nullptr;   //mid equel 5~8
    int* two_selected_tail = nullptr;  //tail equel 9~12
    int five_length[3];
    int two_length[3];
    std::set<int> result_five;
    std::set<int> result_two;
};
#endif //HAPPIEST_HAPPYCASH_H
